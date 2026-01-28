import { useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Template } from '@/api';
import { Clock, Star, ChevronRight } from 'lucide-react';
import { DIFFICULTY_COLORS, DIFFICULTY_LABELS } from '@/utils/constants';

export interface TemplateCardProps {
  template: Template;
  categoryId: string;
}

export function TemplateCard({ template, categoryId }: TemplateCardProps) {
  const navigate = useNavigate();

  const difficultyColor = DIFFICULTY_COLORS[template.difficulty] || DIFFICULTY_COLORS.EASY;
  const difficultyLabel = DIFFICULTY_LABELS[template.difficulty] || 'Easy';

  return (
    <Card
      variant="interactive"
      className="cursor-pointer group"
      onClick={() => navigate(`/create/${categoryId}/${template.id}`)}
    >
      <div className="flex flex-col h-full">
        <div className="flex items-start justify-between mb-3">
          <div className="flex items-center gap-2">
            {template.popular && (
              <Badge variant="warning" size="sm">
                <Star className="h-3 w-3 mr-1" />
                Popular
              </Badge>
            )}
          </div>
          <Badge className={difficultyColor} size="sm">
            {difficultyLabel}
          </Badge>
        </div>

        <h3 className="font-semibold text-slate-900 mb-2 group-hover:text-blue-600 transition-colors">
          {template.name}
        </h3>

        <p className="text-sm text-slate-500 line-clamp-2 mb-4 flex-grow">
          {template.description}
        </p>

        <div className="flex items-center justify-between pt-3 border-t border-slate-100">
          <div className="flex items-center gap-1 text-slate-400">
            <Clock className="h-4 w-4" />
            <span className="text-xs">{template.estimatedTime}</span>
          </div>
          <div className="flex items-center gap-1 text-blue-600 text-sm font-medium opacity-0 group-hover:opacity-100 transition-opacity">
            Start
            <ChevronRight className="h-4 w-4" />
          </div>
        </div>
      </div>
    </Card>
  );
}
