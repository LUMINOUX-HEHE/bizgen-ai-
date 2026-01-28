import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { Badge } from '@/components/common/Badge';
import { Input } from '@/components/common/Input';
import { TextArea } from '@/components/common/TextArea';
import { Select } from '@/components/common/Select';
import { Checkbox } from '@/components/common/Checkbox';
import { RadioGroup } from '@/components/common/RadioGroup';
import { MultiSelect } from '@/components/common/MultiSelect';
import { PageLoader } from '@/components/common/Loader';
import { EmptyState } from '@/components/common/EmptyState';
import { fetchTemplateSchema, generateContent, TemplateSchema, FormField as FormFieldType, GenerateRequest } from '@/api';
import { ArrowLeft, Clock, AlertTriangle, Sparkles } from 'lucide-react';
import { LOCAL_STORAGE_KEYS } from '@/utils/constants';
import toast from 'react-hot-toast';

export function ContentForm() {
  const { categoryId, templateId } = useParams<{ categoryId: string; templateId: string }>();
  const navigate = useNavigate();
  const [schema, setSchema] = useState<TemplateSchema | null>(null);
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);

  // Build dynamic Zod schema
  const buildZodSchema = (schema: TemplateSchema) => {
    const fields: Record<string, z.ZodTypeAny> = {};
    
    schema.sections.forEach(section => {
      section.fields.forEach(field => {
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
            
            if (!field.required) {
              fieldSchema = strSchema.optional().or(z.literal(''));
            } else {
              fieldSchema = strSchema;
            }
            break;
          }
        }
        
        fields[field.name] = fieldSchema;
      });
    });
    
    return z.object(fields);
  };

  // Get default values from schema
  const getDefaultValues = (schema: TemplateSchema) => {
    const values: Record<string, unknown> = {};
    schema.sections.forEach(section => {
      section.fields.forEach(field => {
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
    
    // Load draft from localStorage
    const draftKey = `${LOCAL_STORAGE_KEYS.DRAFT_PREFIX}${templateId}`;
    try {
      const saved = localStorage.getItem(draftKey);
      if (saved) {
        const parsed = JSON.parse(saved);
        return { ...values, ...parsed };
      }
    } catch {
      // Ignore invalid draft
    }
    
    return values;
  };

  useEffect(() => {
    if (templateId) {
      loadSchema();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [templateId]);

  const loadSchema = async () => {
    if (!templateId) return;
    
    setLoading(true);
    try {
      const data = await fetchTemplateSchema(templateId);
      setSchema(data);
    } catch (error) {
      console.error('Failed to load template schema:', error);
      toast.error('Failed to load template. Please try again.');
      navigate(`/create/${categoryId}`);
    } finally {
      setLoading(false);
    }
  };

  const zodSchema = schema ? buildZodSchema(schema) : z.object({});
  const defaultValues = schema ? getDefaultValues(schema) : {};

  const {
    control,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm({
    resolver: zodResolver(zodSchema),
    defaultValues,
  });

  // Auto-save draft
  const formValues = watch();
  useEffect(() => {
    if (!templateId || !schema) return;
    
    const timer = setTimeout(() => {
      const draftKey = `${LOCAL_STORAGE_KEYS.DRAFT_PREFIX}${templateId}`;
      try {
        localStorage.setItem(draftKey, JSON.stringify(formValues));
      } catch {
        // Storage full or unavailable
      }
    }, 1000);

    return () => clearTimeout(timer);
  }, [formValues, templateId, schema]);

  const onSubmit = async (data: Record<string, unknown>) => {
    if (!templateId) return;
    
    setSubmitting(true);
    try {
      const request: GenerateRequest = {
        templateId,
        inputs: data,
      };
      
      const result = await generateContent(request);
      
      // Clear draft on successful generation
      const draftKey = `${LOCAL_STORAGE_KEYS.DRAFT_PREFIX}${templateId}`;
      localStorage.removeItem(draftKey);
      
      toast.success('Content generated successfully!');
      navigate(`/app/output/${result.generationId}`);
    } catch (error: unknown) {
      console.error('Failed to generate content:', error);
      const errorMessage = error && typeof error === 'object' && 'message' in error 
        ? (error as { message: string }).message 
        : 'Failed to generate content. Please try again.';
      toast.error(errorMessage);
    } finally {
      setSubmitting(false);
    }
  };

  const renderField = (field: FormFieldType) => {
    const error = errors[field.name]?.message as string | undefined;
    
    return (
      <Controller
        key={field.id}
        name={field.name}
        control={control}
        render={({ field: formField }) => {
          switch (field.type) {
            case 'text':
              return (
                <Input
                  {...formField}
                  value={formField.value as string || ''}
                  label={field.label}
                  placeholder={field.placeholder}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                  maxLength={field.maxLength || field.validation?.maxLength}
                />
              );

            case 'textarea':
              return (
                <TextArea
                  {...formField}
                  value={formField.value as string || ''}
                  label={field.label}
                  placeholder={field.placeholder}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                  rows={field.rows || 4}
                  maxLength={field.maxLength || field.validation?.maxLength}
                  showCount={!!(field.maxLength || field.validation?.maxLength)}
                />
              );

            case 'select':
              return (
                <Select
                  {...formField}
                  value={formField.value as string || ''}
                  label={field.label}
                  placeholder={field.placeholder || 'Select an option...'}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                  options={field.options || []}
                />
              );

            case 'multiselect':
              return (
                <MultiSelect
                  label={field.label}
                  placeholder={field.placeholder || 'Select options...'}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                  options={field.options || []}
                  value={(formField.value as string[]) || []}
                  onChange={formField.onChange}
                />
              );

            case 'radio':
              return (
                <RadioGroup
                  name={field.name}
                  label={field.label}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                  options={field.options || []}
                  value={formField.value as string || ''}
                  onValueChange={formField.onChange}
                />
              );

            case 'checkbox':
              return (
                <Checkbox
                  name={formField.name}
                  ref={formField.ref}
                  checked={formField.value as boolean || false}
                  onChange={(e) => formField.onChange(e.target.checked)}
                  onBlur={formField.onBlur}
                  label={field.label}
                  helpText={field.helpText}
                  error={error}
                />
              );

            default:
              return (
                <Input
                  {...formField}
                  value={formField.value as string || ''}
                  label={field.label}
                  placeholder={field.placeholder}
                  helpText={field.helpText}
                  error={error}
                  required={field.required}
                />
              );
          }
        }}
      />
    );
  };

  if (loading) {
    return <PageLoader text="Loading form..." />;
  }

  if (!schema) {
    return (
      <EmptyState
        icon="file"
        title="Template not found"
        description="The template you're looking for doesn't exist."
        actionLabel="Go back"
        onAction={() => navigate(`/create/${categoryId}`)}
      />
    );
  }

  const isLegalCategory = schema.category.toLowerCase().includes('legal');

  return (
    <div className="space-y-6 max-w-3xl mx-auto">
      {/* Back Button */}
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(`/create/${categoryId}`)}
        leftIcon={<ArrowLeft className="h-4 w-4" />}
      >
        Back to templates
      </Button>

      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold text-slate-900 mb-2">
          {schema.templateName}
        </h1>
        <p className="text-slate-500 mb-4">
          {schema.description}
        </p>
        <div className="flex items-center gap-4">
          <Badge variant="outline">
            {schema.category}
          </Badge>
          <div className="flex items-center gap-1.5 text-slate-400">
            <Clock className="h-4 w-4" />
            <span className="text-sm">{schema.estimatedTime}</span>
          </div>
        </div>
      </div>

      {/* Legal Disclaimer */}
      {isLegalCategory && (
        <div className="bg-amber-50 border border-amber-200 rounded-lg p-4 flex items-start gap-3">
          <AlertTriangle className="h-5 w-5 text-amber-600 flex-shrink-0 mt-0.5" />
          <div>
            <h4 className="font-medium text-amber-800">Draft Document</h4>
            <p className="text-sm text-amber-700">
              This will generate a draft document that requires review by a qualified legal professional before use.
            </p>
          </div>
        </div>
      )}

      {/* Form */}
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
        {schema.sections
          .sort((a, b) => a.order - b.order)
          .map(section => (
            <Card key={section.id} padding="lg">
              <div className="mb-6">
                <h3 className="text-lg font-semibold text-slate-900">
                  {section.title}
                </h3>
                {section.description && (
                  <p className="mt-1 text-sm text-slate-500">
                    {section.description}
                  </p>
                )}
              </div>
              <div className="space-y-5">
                {section.fields.map(field => renderField(field))}
              </div>
            </Card>
          ))}

        {/* Submit */}
        <Card className="sticky bottom-4">
          <div className="flex items-center justify-between">
            <div className="text-sm text-slate-400">
              Draft saved automatically
            </div>
            <div className="flex items-center gap-3">
              <Button
                type="button"
                variant="secondary"
                onClick={() => navigate(`/create/${categoryId}`)}
                disabled={submitting}
              >
                Cancel
              </Button>
              <Button
                type="submit"
                isLoading={submitting}
                leftIcon={<Sparkles className="h-4 w-4" />}
              >
                {submitting ? 'Generating...' : 'Generate Content'}
              </Button>
            </div>
          </div>
        </Card>
      </form>
    </div>
  );
}
