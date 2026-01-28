import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Badge } from '@/components/common/Badge';
import { PageLoader } from '@/components/common/Loader';
import { EmptyState } from '@/components/common/EmptyState';
import { fetchCategories, Category } from '@/api';
import { Megaphone, Shield, FileText, Mail, BarChart, ArrowRight } from 'lucide-react';
import toast from 'react-hot-toast';

const iconMap: Record<string, React.ElementType> = {
  megaphone: Megaphone,
  shield: Shield,
  file: FileText,
  mail: Mail,
  chart: BarChart,
};

export function CategorySelection() {
  const navigate = useNavigate();
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadCategories();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadCategories = async () => {
    setLoading(true);
    try {
      const data = await fetchCategories();
      setCategories(data);
    } catch (error) {
      console.error('Failed to load categories:', error);
      toast.error('Failed to load categories. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <PageLoader text="Loading categories..." />;
  }

  if (categories.length === 0) {
    return (
      <EmptyState
        icon="folder"
        title="No categories available"
        description="There are no content categories available at the moment."
      />
    );
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="text-center max-w-2xl mx-auto">
        <h1 className="text-3xl font-bold text-slate-900 mb-3">
          What would you like to create?
        </h1>
        <p className="text-lg text-slate-500">
          Select a category to get started with our AI-powered content generation.
        </p>
      </div>

      {/* Category Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 max-w-4xl mx-auto">
        {categories.map((category) => {
          const Icon = iconMap[category.icon] || FileText;
          
          return (
            <Card
              key={category.id}
              variant="interactive"
              padding="lg"
              className="cursor-pointer group"
              onClick={() => navigate(`/app/create/${category.id}`)}
            >
              <div className="flex items-start gap-5">
                <div className="h-14 w-14 rounded-xl bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center flex-shrink-0 group-hover:scale-110 transition-transform duration-200">
                  <Icon className="h-7 w-7 text-white" />
                </div>
                <div className="flex-1 min-w-0">
                  <div className="flex items-center gap-2 mb-2">
                    <h3 className="text-xl font-semibold text-slate-900 group-hover:text-blue-600 transition-colors">
                      {category.displayName}
                    </h3>
                    {category.requiresDisclaimer && (
                      <Badge variant="warning" size="sm">
                        Draft Only
                      </Badge>
                    )}
                  </div>
                  <p className="text-slate-500 mb-4 line-clamp-2">
                    {category.description}
                  </p>
                  <div className="flex items-center justify-between">
                    <Badge variant="outline">
                      {category.templateCount} template{category.templateCount !== 1 ? 's' : ''}
                    </Badge>
                    <div className="flex items-center gap-1 text-blue-600 font-medium text-sm opacity-0 group-hover:opacity-100 transition-opacity">
                      Browse templates
                      <ArrowRight className="h-4 w-4" />
                    </div>
                  </div>
                </div>
              </div>
            </Card>
          );
        })}
      </div>

      {/* Info Note */}
      <div className="text-center text-sm text-slate-400 max-w-lg mx-auto">
        <p>
          All content is generated using AI. Marketing content is ready to use, 
          while legal documents are drafts that require professional review.
        </p>
      </div>
    </div>
  );
}
