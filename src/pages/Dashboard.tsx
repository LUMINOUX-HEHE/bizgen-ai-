import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { Badge } from '@/components/common/Badge';
import { Skeleton } from '@/components/common/Skeleton';
import { EmptyState } from '@/components/common/EmptyState';
import { fetchCategories, fetchHistory, fetchPopularTemplates, Category, HistoryItem, Template } from '@/api';
import { formatRelativeTime } from '@/utils/formatters';
import { PlusCircle, TrendingUp, Clock, Sparkles, ArrowRight, FileText, Zap } from 'lucide-react';
import toast from 'react-hot-toast';

export function Dashboard() {
  const navigate = useNavigate();
  const [categories, setCategories] = useState<Category[]>([]);
  const [recentHistory, setRecentHistory] = useState<HistoryItem[]>([]);
  const [popularTemplates, setPopularTemplates] = useState<Template[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadDashboardData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadDashboardData = async () => {
    setLoading(true);
    try {
      const [categoriesData, historyData, popularData] = await Promise.all([
        fetchCategories(),
        fetchHistory(0, 5),
        fetchPopularTemplates().catch(() => []),
      ]);
      setCategories(categoriesData);
      setRecentHistory(historyData.content);
      setPopularTemplates(popularData);
    } catch (error) {
      console.error('Failed to load dashboard data:', error);
      toast.error('Failed to load dashboard data');
    } finally {
      setLoading(false);
    }
  };

  const totalTemplates = categories.reduce((sum, cat) => sum + cat.templateCount, 0);
  const totalGenerations = recentHistory.length;

  if (loading) {
    return (
      <div className="space-y-8">
        <Skeleton className="h-32 w-full" />
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Skeleton className="h-28" />
          <Skeleton className="h-28" />
          <Skeleton className="h-28" />
        </div>
        <Skeleton className="h-64 w-full" />
      </div>
    );
  }

  return (
    <div className="space-y-8">
      {/* Welcome Hero */}
      <Card className="bg-gradient-to-br from-blue-600 to-blue-700 border-0 text-white" padding="lg">
        <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-6">
          <div>
            <h2 className="text-2xl font-bold mb-2">Welcome to BizGen AI</h2>
            <p className="text-blue-100 max-w-lg">
              Create professional marketing content and legal documents in minutes using our AI-powered templates.
            </p>
          </div>
          <Button
            onClick={() => navigate('/app/create')}
            className="bg-white text-blue-600 hover:bg-blue-50 shadow-lg"
            size="lg"
            leftIcon={<PlusCircle className="h-5 w-5" />}
          >
            Create New Content
          </Button>
        </div>
      </Card>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card className="flex items-center gap-4">
          <div className="h-12 w-12 rounded-xl bg-blue-100 flex items-center justify-center">
            <FileText className="h-6 w-6 text-blue-600" />
          </div>
          <div>
            <p className="text-2xl font-bold text-slate-900">{totalTemplates}</p>
            <p className="text-sm text-slate-500">Available Templates</p>
          </div>
        </Card>

        <Card className="flex items-center gap-4">
          <div className="h-12 w-12 rounded-xl bg-emerald-100 flex items-center justify-center">
            <Zap className="h-6 w-6 text-emerald-600" />
          </div>
          <div>
            <p className="text-2xl font-bold text-slate-900">{totalGenerations}</p>
            <p className="text-sm text-slate-500">Recent Generations</p>
          </div>
        </Card>

        <Card className="flex items-center gap-4">
          <div className="h-12 w-12 rounded-xl bg-amber-100 flex items-center justify-center">
            <TrendingUp className="h-6 w-6 text-amber-600" />
          </div>
          <div>
            <p className="text-2xl font-bold text-slate-900">{categories.length}</p>
            <p className="text-sm text-slate-500">Categories</p>
          </div>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        {/* Quick Actions */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold text-slate-900">Quick Actions</h3>
          <div className="grid gap-4">
            {categories.map((category) => (
              <Card
                key={category.id}
                variant="interactive"
                className="cursor-pointer"
                onClick={() => navigate(`/app/create/${category.id}`)}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <div className="h-10 w-10 rounded-lg bg-blue-100 flex items-center justify-center">
                      <Sparkles className="h-5 w-5 text-blue-600" />
                    </div>
                    <div>
                      <h4 className="font-medium text-slate-900">{category.displayName}</h4>
                      <p className="text-sm text-slate-500">
                        {category.templateCount} template{category.templateCount !== 1 ? 's' : ''}
                      </p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    {category.requiresDisclaimer && (
                      <Badge variant="warning" size="sm">Draft Only</Badge>
                    )}
                    <ArrowRight className="h-5 w-5 text-slate-400" />
                  </div>
                </div>
              </Card>
            ))}
          </div>
        </div>

        {/* Popular Templates */}
        <div className="space-y-4">
          <h3 className="text-lg font-semibold text-slate-900">Popular Templates</h3>
          {popularTemplates.length > 0 ? (
            <div className="space-y-3">
              {popularTemplates.slice(0, 4).map((template) => (
                <Card
                  key={template.id}
                  variant="interactive"
                  padding="sm"
                  className="cursor-pointer"
                  onClick={() => navigate(`/app/create/${template.categoryId}/${template.id}`)}
                >
                  <div className="flex items-center justify-between">
                    <div>
                      <h4 className="font-medium text-slate-900">{template.name}</h4>
                      <p className="text-sm text-slate-500">{template.categoryName}</p>
                    </div>
                    <div className="flex items-center gap-2 text-slate-400">
                      <Clock className="h-4 w-4" />
                      <span className="text-xs">{template.estimatedTime}</span>
                    </div>
                  </div>
                </Card>
              ))}
            </div>
          ) : (
            <Card className="py-8">
              <EmptyState
                icon="file"
                title="No popular templates"
                description="Start generating content to see your favorites here."
              />
            </Card>
          )}
        </div>
      </div>

      {/* Recent Generations */}
      <div className="space-y-4">
        <div className="flex items-center justify-between">
          <h3 className="text-lg font-semibold text-slate-900">Recent Generations</h3>
          {recentHistory.length > 0 && (
            <Button variant="ghost" size="sm" onClick={() => navigate('/app/history')}>
              View All
            </Button>
          )}
        </div>

        {recentHistory.length > 0 ? (
          <div className="space-y-3">
            {recentHistory.map((item) => (
              <Card
                key={item.id}
                variant="interactive"
                padding="sm"
                className="cursor-pointer"
                onClick={() => navigate(`/app/output/${item.id}`)}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <div className="h-10 w-10 rounded-lg bg-slate-100 flex items-center justify-center">
                      <FileText className="h-5 w-5 text-slate-600" />
                    </div>
                    <div>
                      <h4 className="font-medium text-slate-900">{item.templateName}</h4>
                      <p className="text-sm text-slate-500">{item.categoryName}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-4">
                    <Badge variant="success" size="sm">
                      {item.variationCount} variations
                    </Badge>
                    <span className="text-sm text-slate-400">
                      {formatRelativeTime(item.createdAt)}
                    </span>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        ) : (
          <Card className="py-12">
            <EmptyState
              icon="inbox"
              title="No generations yet"
              description="Create your first content to see it here."
              actionLabel="Create Content"
              onAction={() => navigate('/app/create')}
            />
          </Card>
        )}
      </div>
    </div>
  );
}
