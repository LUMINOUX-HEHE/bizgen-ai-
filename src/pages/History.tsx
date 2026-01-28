import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { Badge } from '@/components/common/Badge';
import { PageLoader } from '@/components/common/Loader';
import { EmptyState } from '@/components/common/EmptyState';
import { Modal } from '@/components/common/Modal';
import { fetchHistory, deleteHistoryItem, HistoryItem, PageResponse } from '@/api';
import { formatRelativeTime } from '@/utils/formatters';
import { STATUS_COLORS } from '@/utils/constants';
import { FileText, Trash2, Eye, ChevronLeft, ChevronRight, Clock, AlertTriangle } from 'lucide-react';
import toast from 'react-hot-toast';

export function History() {
  const navigate = useNavigate();
  const [history, setHistory] = useState<PageResponse<HistoryItem> | null>(null);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(0);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [itemToDelete, setItemToDelete] = useState<HistoryItem | null>(null);
  const [deleting, setDeleting] = useState(false);

  const pageSize = 10;

  useEffect(() => {
    loadHistory();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [page]);

  const loadHistory = async () => {
    setLoading(true);
    try {
      const data = await fetchHistory(page, pageSize);
      setHistory(data);
    } catch (error) {
      console.error('Failed to load history:', error);
      toast.error('Failed to load history. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!itemToDelete) return;
    
    setDeleting(true);
    try {
      await deleteHistoryItem(itemToDelete.id);
      toast.success('History item deleted');
      setDeleteModalOpen(false);
      setItemToDelete(null);
      loadHistory();
    } catch (error) {
      console.error('Failed to delete history item:', error);
      toast.error('Failed to delete. Please try again.');
    } finally {
      setDeleting(false);
    }
  };

  const openDeleteModal = (item: HistoryItem, e: React.MouseEvent) => {
    e.stopPropagation();
    setItemToDelete(item);
    setDeleteModalOpen(true);
  };

  if (loading && !history) {
    return <PageLoader text="Loading history..." />;
  }

  const hasItems = history && history.content.length > 0;
  const totalPages = history ? history.totalPages : 0;

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold text-slate-900 mb-1">
            Generation History
          </h1>
          <p className="text-slate-500">
            {history ? `${history.totalElements} generation${history.totalElements !== 1 ? 's' : ''}` : 'Loading...'}
          </p>
        </div>
        <Button onClick={() => navigate('/create')}>
          Create New
        </Button>
      </div>

      {/* History List */}
      {hasItems ? (
        <div className="space-y-3">
          {history.content.map((item) => {
            const statusColor = STATUS_COLORS[item.status as keyof typeof STATUS_COLORS] || STATUS_COLORS.COMPLETED;
            
            return (
              <Card
                key={item.id}
                variant="interactive"
                padding="md"
                className="cursor-pointer"
                onClick={() => navigate(`/app/output/${item.id}`)}
              >
                <div className="flex items-center justify-between">
                  <div className="flex items-center gap-4">
                    <div className="h-12 w-12 rounded-lg bg-slate-100 flex items-center justify-center flex-shrink-0">
                      <FileText className="h-6 w-6 text-slate-600" />
                    </div>
                    <div>
                      <h3 className="font-medium text-slate-900">
                        {item.templateName}
                      </h3>
                      <p className="text-sm text-slate-500">
                        {item.categoryName}
                      </p>
                    </div>
                  </div>

                  <div className="flex items-center gap-6">
                    <div className="hidden md:flex items-center gap-4 text-sm text-slate-500">
                      <span className="flex items-center gap-1.5">
                        <Clock className="h-4 w-4" />
                        {formatRelativeTime(item.createdAt)}
                      </span>
                    </div>

                    <div className="flex items-center gap-2">
                      <Badge variant="success" size="sm">
                        {item.variationCount} variations
                      </Badge>
                      <Badge className={statusColor} size="sm">
                        {item.status}
                      </Badge>
                    </div>

                    <div className="flex items-center gap-2">
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={(e) => {
                          e.stopPropagation();
                          navigate(`/app/output/${item.id}`);
                        }}
                      >
                        <Eye className="h-4 w-4" />
                      </Button>
                      <Button
                        variant="ghost"
                        size="sm"
                        onClick={(e) => openDeleteModal(item, e)}
                        className="text-red-500 hover:text-red-600 hover:bg-red-50"
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                </div>
              </Card>
            );
          })}
        </div>
      ) : (
        <Card className="py-16">
          <EmptyState
            icon="inbox"
            title="No generations yet"
            description="Create your first content to see it here."
            actionLabel="Create Content"
            onAction={() => navigate('/create')}
          />
        </Card>
      )}

      {/* Pagination */}
      {hasItems && totalPages > 1 && (
        <div className="flex items-center justify-between border-t border-slate-200 pt-4">
          <div className="text-sm text-slate-500">
            Page {page + 1} of {totalPages}
          </div>
          <div className="flex items-center gap-2">
            <Button
              variant="secondary"
              size="sm"
              onClick={() => setPage(page - 1)}
              disabled={page === 0 || loading}
              leftIcon={<ChevronLeft className="h-4 w-4" />}
            >
              Previous
            </Button>
            <Button
              variant="secondary"
              size="sm"
              onClick={() => setPage(page + 1)}
              disabled={page >= totalPages - 1 || loading}
              rightIcon={<ChevronRight className="h-4 w-4" />}
            >
              Next
            </Button>
          </div>
        </div>
      )}

      {/* Delete Confirmation Modal */}
      <Modal
        isOpen={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        title="Delete Generation"
        size="sm"
      >
        <div className="space-y-4">
          <div className="flex items-start gap-3">
            <div className="h-10 w-10 rounded-full bg-red-100 flex items-center justify-center flex-shrink-0">
              <AlertTriangle className="h-5 w-5 text-red-600" />
            </div>
            <div>
              <p className="text-slate-900">
                Are you sure you want to delete this generation?
              </p>
              {itemToDelete && (
                <p className="text-sm text-slate-500 mt-1">
                  "{itemToDelete.templateName}" will be permanently removed.
                </p>
              )}
            </div>
          </div>
          <div className="flex items-center justify-end gap-3">
            <Button
              variant="secondary"
              onClick={() => setDeleteModalOpen(false)}
              disabled={deleting}
            >
              Cancel
            </Button>
            <Button
              variant="danger"
              onClick={handleDelete}
              isLoading={deleting}
            >
              Delete
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
}
