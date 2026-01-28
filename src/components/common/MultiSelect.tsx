import { forwardRef, useState } from 'react';
import { cn } from '@/utils/cn';
import { Check, X } from 'lucide-react';

export interface MultiSelectOption {
  value: string;
  label: string;
}

export interface MultiSelectProps {
  label?: string;
  error?: string;
  helpText?: string;
  options: MultiSelectOption[];
  value?: string[];
  onChange?: (value: string[]) => void;
  required?: boolean;
  placeholder?: string;
  className?: string;
}

export const MultiSelect = forwardRef<HTMLDivElement, MultiSelectProps>(
  (
    {
      label,
      error,
      helpText,
      options,
      value = [],
      onChange,
      required,
      placeholder = 'Select options...',
      className,
    },
    ref
  ) => {
    const [isOpen, setIsOpen] = useState(false);

    const toggleOption = (optionValue: string) => {
      const newValue = value.includes(optionValue)
        ? value.filter((v) => v !== optionValue)
        : [...value, optionValue];
      onChange?.(newValue);
    };

    const removeOption = (optionValue: string, e: React.MouseEvent) => {
      e.stopPropagation();
      onChange?.(value.filter((v) => v !== optionValue));
    };

    const selectedLabels = value
      .map((v) => options.find((o) => o.value === v)?.label)
      .filter(Boolean);

    return (
      <div ref={ref} className={cn('w-full relative', className)}>
        {label && (
          <label className="block text-sm font-medium text-slate-700 mb-1.5">
            {label}
            {required && <span className="text-red-500 ml-0.5">*</span>}
          </label>
        )}

        {/* Selected values display / trigger */}
        <div
          onClick={() => setIsOpen(!isOpen)}
          className={cn(
            'min-h-[42px] w-full rounded-lg border bg-white px-3 py-2 cursor-pointer transition-colors',
            'focus:outline-none focus:ring-2 focus:ring-blue-500/20',
            error
              ? 'border-red-500'
              : isOpen
                ? 'border-blue-500 ring-2 ring-blue-500/20'
                : 'border-slate-300 hover:border-slate-400'
          )}
        >
          {value.length === 0 ? (
            <span className="text-slate-400 text-sm">{placeholder}</span>
          ) : (
            <div className="flex flex-wrap gap-1">
              {selectedLabels.map((label, i) => (
                <span
                  key={value[i]}
                  className="inline-flex items-center gap-1 px-2 py-0.5 bg-blue-100 text-blue-700 text-xs font-medium rounded-full"
                >
                  {label}
                  <button
                    type="button"
                    onClick={(e) => removeOption(value[i], e)}
                    className="hover:bg-blue-200 rounded-full p-0.5"
                  >
                    <X className="h-3 w-3" />
                  </button>
                </span>
              ))}
            </div>
          )}
        </div>

        {/* Dropdown */}
        {isOpen && (
          <>
            <div
              className="fixed inset-0 z-10"
              onClick={() => setIsOpen(false)}
            />
            <div className="absolute z-20 w-full mt-1 bg-white border border-slate-200 rounded-lg shadow-lg max-h-60 overflow-auto">
              {options.map((option) => (
                <div
                  key={option.value}
                  onClick={() => toggleOption(option.value)}
                  className={cn(
                    'flex items-center gap-3 px-3 py-2 cursor-pointer transition-colors',
                    value.includes(option.value)
                      ? 'bg-blue-50 text-blue-700'
                      : 'hover:bg-slate-50'
                  )}
                >
                  <div
                    className={cn(
                      'h-4 w-4 rounded border flex items-center justify-center transition-colors',
                      value.includes(option.value)
                        ? 'bg-blue-600 border-blue-600'
                        : 'border-slate-300'
                    )}
                  >
                    {value.includes(option.value) && (
                      <Check className="h-3 w-3 text-white" />
                    )}
                  </div>
                  <span className="text-sm">{option.label}</span>
                </div>
              ))}
            </div>
          </>
        )}

        {error && <p className="mt-1 text-sm text-red-600">{error}</p>}
        {helpText && !error && (
          <p className="mt-1 text-sm text-slate-500">{helpText}</p>
        )}
      </div>
    );
  }
);

MultiSelect.displayName = 'MultiSelect';
