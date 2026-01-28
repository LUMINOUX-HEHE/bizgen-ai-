import { useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { Category } from '@/api';
import { Megaphone, Shield, FileText, Mail, BarChart } from 'lucide-react';

const iconMap: Record<string, React.ElementType> = {
  megaphone: Megaphone,
  shield: Shield,
  file: FileText,
  mail: Mail,
  chart: BarChart,
};

export interface CategoryCardProps {
  category: Category;
}

export function CategoryCard({ category }: CategoryCardProps) {
  const navigate = useNavigate();
  const Icon = iconMap[category.icon] || FileText;

  return (
    <Card
      variant="interactive"
      className="cursor-pointer"
      onClick={() => navigate(`/create/${category.id}`)}
    >
      <div className="flex items-start gap-4">
        <div className="h-12 w-12 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center flex-shrink-0">
          <Icon className="h-6 w-6 text-white" />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2 mb-1">
            <h3 className="font-semibold text-slate-900">{category.displayName}</h3>
            {category.requiresDisclaimer && (
              <Badge variant="warning" size="sm">
                Draft Only
              </Badge>
            )}
          </div>
          <p className="text-sm text-slate-500 line-clamp-2 mb-3">
            {category.description}
          </p>
          <Badge variant="outline" size="sm">
            {category.templateCount} template{category.templateCount !== 1 ? 's' : ''}
          </Badge>
        </div>
      </div>
    </Card>
  );
}
