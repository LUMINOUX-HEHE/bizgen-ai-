import { AlertTriangle } from 'lucide-react';
import { cn } from '@/utils/cn';

export interface DisclaimerBannerProps {
  disclaimers: string[];
  className?: string;
}

export function DisclaimerBanner({ disclaimers, className }: DisclaimerBannerProps) {
  if (disclaimers.length === 0) return null;

  return (
    <div
      className={cn(
        'bg-amber-50 border border-amber-200 rounded-lg p-4',
        className
      )}
    >
      <div className="flex items-start gap-3">
        <AlertTriangle className="h-5 w-5 text-amber-600 flex-shrink-0 mt-0.5" />
        <div>
          <h4 className="font-semibold text-amber-800 mb-2">Important Notice</h4>
          <ul className="space-y-1">
            {disclaimers.map((disclaimer, index) => (
              <li key={index} className="text-sm text-amber-700">
                • {disclaimer}
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
