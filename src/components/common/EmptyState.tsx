import { cn } from '@/utils/cn';
import { FileText, FolderOpen, Inbox, Search } from 'lucide-react';
import { Button } from './Button';

export interface EmptyStateProps {
  icon?: 'inbox' | 'folder' | 'file' | 'search';
  title: string;
  description?: string;
  actionLabel?: string;
  onAction?: () => void;
  className?: string;
}

export function EmptyState({
  icon = 'inbox',
  title,
  description,
  actionLabel,
  onAction,
  className,
}: EmptyStateProps) {
  const icons = {
    inbox: Inbox,
    folder: FolderOpen,
    file: FileText,
    search: Search,
  };

  const Icon = icons[icon];

  return (
    <div
      className={cn(
        'flex flex-col items-center justify-center text-center py-12 px-6',
        className
      )}
    >
      <div className="h-16 w-16 rounded-full bg-slate-100 flex items-center justify-center mb-4">
        <Icon className="h-8 w-8 text-slate-400" />
      </div>
      <h3 className="text-lg font-semibold text-slate-900 mb-2">{title}</h3>
      {description && (
        <p className="text-sm text-slate-500 max-w-sm mb-6">{description}</p>
      )}
      {actionLabel && onAction && (
        <Button onClick={onAction}>{actionLabel}</Button>
      )}
    </div>
  );
}
