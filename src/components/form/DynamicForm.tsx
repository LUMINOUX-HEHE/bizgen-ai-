import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { TemplateSchema } from '@/api';
import { FormSection } from './FormSection';
import { Button } from '@/components/common/Button';
import { useMemo, useEffect } from 'react';
import { LOCAL_STORAGE_KEYS } from '@/utils/constants';

export interface DynamicFormProps {
  schema: TemplateSchema;
  onSubmit: (data: Record<string, unknown>) => void;
  isSubmitting?: boolean;
  onCancel?: () => void;
}

export function DynamicForm({
  schema,
  onSubmit,
  isSubmitting,
  onCancel,
}: DynamicFormProps) {
  // Build Zod validation schema from form fields
  const zodSchema = useMemo(() => {
    const schemaFields: Record<string, z.ZodTypeAny> = {};

    schema.sections.forEach((section) => {
      section.fields.forEach((field) => {
        let fieldSchema: z.ZodTypeAny;

        switch (field.type) {
          case 'checkbox':
            fieldSchema = z.boolean().optional();
            break;
          case 'multiselect':
            if (field.required) {
              fieldSchema = z.array(z.string()).min(1, `${field.label} is required`);
            } else {
              fieldSchema = z.array(z.string()).optional();
            }
            break;
          default: {
            let strSchema = z.string();
            
            if (field.required) {
              strSchema = strSchema.min(1, `${field.label} is required`);
            }
            if (field.validation?.minLength && field.required) {
              strSchema = strSchema.min(
                field.validation.minLength,
                `${field.label} must be at least ${field.validation.minLength} characters`
              );
            }
            if (field.validation?.maxLength) {
              strSchema = strSchema.max(
                field.validation.maxLength,
                `${field.label} must not exceed ${field.validation.maxLength} characters`
              );
            }
            if (field.validation?.pattern) {
              try {
                const regex = new RegExp(field.validation.pattern);
                strSchema = strSchema.regex(regex, `${field.label} has an invalid format`);
              } catch {
                // Invalid regex, skip
              }
            }
            
            if (!field.required) {
              fieldSchema = strSchema.optional().or(z.literal(''));
            } else {
              fieldSchema = strSchema;
            }
            break;
          }
        }

        schemaFields[field.name] = fieldSchema;
      });
    });

    return z.object(schemaFields);
  }, [schema]);

  // Get default values
  const defaultValues = useMemo(() => {
    const values: Record<string, unknown> = {};
    schema.sections.forEach((section) => {
      section.fields.forEach((field) => {
        if (field.defaultValue !== undefined) {
          values[field.name] = field.defaultValue;
        } else if (field.type === 'multiselect') {
          values[field.name] = [];
        } else if (field.type === 'checkbox') {
          values[field.name] = false;
        } else {
          values[field.name] = '';
        }
      });
    });
    return values;
  }, [schema]);

  // Load draft from localStorage
  const draftKey = `${LOCAL_STORAGE_KEYS.DRAFT_PREFIX}${schema.templateId}`;
  const savedDraft = useMemo(() => {
    try {
      const saved = localStorage.getItem(draftKey);
      if (saved) {
        const parsed = JSON.parse(saved);
        // Merge with defaults
        return { ...defaultValues, ...parsed };
      }
    } catch {
      // Invalid draft, ignore
    }
    return defaultValues;
  }, [draftKey, defaultValues]);

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(zodSchema),
    defaultValues: savedDraft,
  });

  // Auto-save draft
  const formValues = watch();
  useEffect(() => {
    const timer = setTimeout(() => {
      try {
        localStorage.setItem(draftKey, JSON.stringify(formValues));
      } catch {
        // Storage full or unavailable
      }
    }, 1000);

    return () => clearTimeout(timer);
  }, [formValues, draftKey]);

  const handleFormSubmit = (data: Record<string, unknown>) => {
    // Clear draft on successful submit
    localStorage.removeItem(draftKey);
    onSubmit(data);
  };

  return (
    <form onSubmit={handleSubmit(handleFormSubmit)} className="space-y-6">
      {schema.sections
        .sort((a, b) => a.order - b.order)
        .map((section) => (
          <FormSection
            key={section.id}
            section={section}
            control={control}
            errors={errors}
          />
        ))}

      <div className="flex items-center justify-between pt-4 border-t border-slate-200">
        <div className="text-xs text-slate-400">
          Draft saved automatically
        </div>
        <div className="flex items-center gap-3">
          {onCancel && (
            <Button
              type="button"
              variant="secondary"
              onClick={onCancel}
              disabled={isSubmitting}
            >
              Cancel
            </Button>
          )}
          <Button type="submit" isLoading={isSubmitting}>
            Generate Content
          </Button>
        </div>
      </div>
    </form>
  );
}
