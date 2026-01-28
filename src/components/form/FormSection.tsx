import { Control, FieldErrors } from 'react-hook-form';
import { FormSection as FormSectionType } from '@/api';
import { FormField } from './FormField';

export interface FormSectionProps {
  section: FormSectionType;
  control: Control<Record<string, unknown>>;
  errors: FieldErrors;
}

export function FormSection({ section, control, errors }: FormSectionProps) {
  return (
    <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-sm">
      <div className="mb-6">
        <h3 className="text-lg font-semibold text-slate-900">{section.title}</h3>
        {section.description && (
          <p className="mt-1 text-sm text-slate-500">{section.description}</p>
        )}
      </div>
      <div className="space-y-5">
        {section.fields.map((field) => (
          <FormField
            key={field.id}
            field={field}
            control={control}
            errors={errors}
          />
        ))}
      </div>
    </div>
  );
}
