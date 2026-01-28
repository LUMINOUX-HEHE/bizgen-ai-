import { Template } from '@/api';
import { TemplateCard } from './TemplateCard';
import { TemplateCardSkeleton } from '@/components/common/Skeleton';
import { EmptyState } from '@/components/common/EmptyState';

export interface TemplateGridProps {
  templates: Template[];
  categoryId: string;
  loading?: boolean;
}

export function TemplateGrid({ templates, categoryId, loading }: TemplateGridProps) {
  if (loading) {
    return (
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {[...Array(6)].map((_, i) => (
          <TemplateCardSkeleton key={i} />
        ))}
      </div>
    );
  }

  if (templates.length === 0) {
    return (
      <EmptyState
        icon="file"
        title="No templates found"
        description="There are no templates available in this category yet."
      />
    );
  }

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      {templates.map((template) => (
        <TemplateCard
          key={template.id}
          template={template}
          categoryId={categoryId}
        />
      ))}
    </div>
  );
}
