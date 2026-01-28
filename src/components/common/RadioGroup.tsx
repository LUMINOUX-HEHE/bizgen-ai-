import { forwardRef, InputHTMLAttributes } from 'react';
import { cn } from '@/utils/cn';

export interface RadioOption {
  value: string;
  label: string;
}

export interface RadioGroupProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label?: string;
  error?: string;
  helpText?: string;
  options: RadioOption[];
  value?: string;
  onValueChange?: (value: string) => void;
}

export const RadioGroup = forwardRef<HTMLInputElement, RadioGroupProps>(
  ({ className, label, error, helpText, options, value, onValueChange, name, ...props }, ref) => {
    return (
      <div className={cn('w-full', className)}>
        {label && (
          <label className="block text-sm font-medium text-slate-700 mb-2">
            {label}
            {props.required && <span className="text-red-500 ml-0.5">*</span>}
          </label>
        )}
        <div className="space-y-2">
          {options.map((option) => (
            <label
              key={option.value}
              className={cn(
                'flex items-center gap-3 p-3 rounded-lg border cursor-pointer transition-all duration-150',
                value === option.value
                  ? 'border-blue-500 bg-blue-50'
                  : 'border-slate-200 hover:border-slate-300 hover:bg-slate-50'
              )}
            >
              <input
                ref={ref}
                type="radio"
                name={name}
                value={option.value}
                checked={value === option.value}
                onChange={(e) => onValueChange?.(e.target.value)}
                className="sr-only"
                {...props}
              />
              <div
                className={cn(
                  'h-5 w-5 rounded-full border-2 flex items-center justify-center transition-all',
                  value === option.value
                    ? 'border-blue-600'
                    : 'border-slate-300'
                )}
              >
                {value === option.value && (
                  <div className="h-2.5 w-2.5 rounded-full bg-blue-600" />
                )}
              </div>
              <span className="text-sm font-medium text-slate-700">
                {option.label}
              </span>
            </label>
          ))}
        </div>
        {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
        {helpText && !error && (
          <p className="mt-1 text-sm text-slate-500">{helpText}</p>
        )}
      </div>
    );
  }
);

RadioGroup.displayName = 'RadioGroup';
