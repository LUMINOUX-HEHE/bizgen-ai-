import { Variation } from '@/api';
import { Card } from '@/components/common/Card';
import { CopyButton } from './CopyButton';
import { Badge } from '@/components/common/Badge';
import { cn } from '@/utils/cn';

export interface VariationCardProps {
  variation: Variation;
  isActive?: boolean;
  className?: string;
}

export function VariationCard({
  variation,
  isActive = true,
  className,
}: VariationCardProps) {
  // Highlight placeholders in content
  const highlightPlaceholders = (content: string) => {
    return content.replace(
      /\[([A-Z][A-Z_0-9]*)\]/g,
      '<span class="bg-amber-100 text-amber-800 px-1 rounded font-medium">[$1]</span>'
    );
  };

  return (
    <Card
      className={cn(
        'transition-opacity duration-200',
        !isActive && 'opacity-50',
        className
      )}
      padding="lg"
    >
      <div className="flex items-center justify-between mb-4">
        <Badge variant="primary">Variation {variation.variationNumber}</Badge>
        <CopyButton text={variation.content} />
      </div>

      {variation.placeholders && variation.placeholders.length > 0 && (
        <div className="mb-4 flex flex-wrap gap-2">
          <span className="text-xs text-slate-500">Needs attention:</span>
          {variation.placeholders.map((placeholder, index) => (
            <Badge key={index} variant="warning" size="sm">
              {placeholder.replace(/_/g, ' ').toLowerCase()}
            </Badge>
          ))}
        </div>
      )}

      <div
        className="prose prose-slate max-w-none text-sm leading-relaxed whitespace-pre-wrap"
        dangerouslySetInnerHTML={{
          __html: highlightPlaceholders(variation.content),
        }}
      />
    </Card>
  );
}
