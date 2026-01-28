import { InputHTMLAttributes, forwardRef } from 'react';
import { cn } from '@/utils/cn';
import { Check } from 'lucide-react';

export interface CheckboxProps extends Omit<InputHTMLAttributes<HTMLInputElement>, 'type'> {
  label?: string;
  error?: string;
  helpText?: string;
}

export const Checkbox = forwardRef<HTMLInputElement, CheckboxProps>(
  ({ className, label, error, helpText, id, ...props }, ref) => {
    const inputId = id || label?.toLowerCase().replace(/\s+/g, '-');

    return (
      <div className="w-full">
        <label
          htmlFor={inputId}
          className={cn(
            'flex items-start gap-3 cursor-pointer group',
            props.disabled && 'cursor-not-allowed opacity-50'
          )}
        >
          <div className="relative flex-shrink-0 mt-0.5">
            <input
              ref={ref}
              type="checkbox"
              id={inputId}
              className="sr-only peer"
              {...props}
            />
            <div
              className={cn(
                'h-5 w-5 rounded border-2 transition-all duration-150',
                'peer-focus:ring-2 peer-focus:ring-blue-500/20',
                'peer-checked:bg-blue-600 peer-checked:border-blue-600',
                error
                  ? 'border-red-500'
                  : 'border-slate-300 group-hover:border-slate-400'
              )}
            />
            <Check className="absolute top-0.5 left-0.5 h-4 w-4 text-white opacity-0 peer-checked:opacity-100 transition-opacity" />
          </div>
          <div className="flex-1">
            {label && (
              <span className="text-sm font-medium text-slate-700">{label}</span>
            )}
            {helpText && (
              <p className="text-sm text-slate-500 mt-0.5">{helpText}</p>
            )}
          </div>
        </label>
        {error && <p className="mt-1 text-sm text-red-600 ml-8">{error}</p>}
      </div>
    );
  }
);

Checkbox.displayName = 'Checkbox';
