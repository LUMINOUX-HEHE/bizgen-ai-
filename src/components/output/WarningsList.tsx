import { useState } from 'react';
import { AlertCircle, ChevronDown, ChevronUp } from 'lucide-react';
import { cn } from '@/utils/cn';

export interface WarningsListProps {
  warnings: string[];
  className?: string;
}

export function WarningsList({ warnings, className }: WarningsListProps) {
  const [isExpanded, setIsExpanded] = useState(warnings.length <= 3);

  if (warnings.length === 0) return null;

  const displayedWarnings = isExpanded ? warnings : warnings.slice(0, 3);

  return (
    <div
      className={cn(
        'bg-blue-50 border border-blue-200 rounded-lg p-4',
        className
      )}
    >
      <div className="flex items-start gap-3">
        <AlertCircle className="h-5 w-5 text-blue-600 flex-shrink-0 mt-0.5" />
        <div className="flex-1">
          <h4 className="font-semibold text-blue-800 mb-2">
            Review Notes ({warnings.length})
          </h4>
          <ul className="space-y-1">
            {displayedWarnings.map((warning, index) => (
              <li key={index} className="text-sm text-blue-700">
                • {warning}
              </li>
            ))}
          </ul>
          {warnings.length > 3 && (
            <button
              onClick={() => setIsExpanded(!isExpanded)}
              className="mt-2 text-sm font-medium text-blue-600 hover:text-blue-700 flex items-center gap-1"
            >
              {isExpanded ? (
                <>
                  Show less <ChevronUp className="h-4 w-4" />
                </>
              ) : (
                <>
                  Show {warnings.length - 3} more <ChevronDown className="h-4 w-4" />
                </>
              )}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
