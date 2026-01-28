import { Controller, Control, FieldErrors } from 'react-hook-form';
import { Input } from '@/components/common/Input';
import { TextArea } from '@/components/common/TextArea';
import { Select } from '@/components/common/Select';
import { Checkbox } from '@/components/common/Checkbox';
import { RadioGroup } from '@/components/common/RadioGroup';
import { MultiSelect } from '@/components/common/MultiSelect';
import { FormField as FormFieldType } from '@/api';

export interface FormFieldProps {
  field: FormFieldType;
  control: Control<Record<string, unknown>>;
  errors: FieldErrors;
}

export function FormField({ field, control, errors }: FormFieldProps) {
  const error = errors[field.name]?.message as string | undefined;

  return (
    <Controller
      name={field.name}
      control={control}
      defaultValue={field.defaultValue ?? (field.type === 'multiselect' ? [] : field.type === 'checkbox' ? false : '')}
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
}
