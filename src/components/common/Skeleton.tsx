import { cn } from '@/utils/cn';

export interface SkeletonProps {
  className?: string;
  variant?: 'text' | 'title' | 'card' | 'avatar' | 'button';
}

export function Skeleton({ className, variant = 'text' }: SkeletonProps) {
  const variants = {
    text: 'h-4 w-full',
    title: 'h-6 w-3/4',
    card: 'h-32 w-full',
    avatar: 'h-10 w-10 rounded-full',
    button: 'h-10 w-24',
  };

  return (
    <div
      className={cn(
        'animate-pulse bg-slate-200 rounded',
        variants[variant],
        className
      )}
    />
  );
}

export function CardSkeleton() {
  return (
    <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-lg shadow-slate-200/50">
      <Skeleton variant="title" className="mb-4" />
      <Skeleton variant="text" className="mb-2" />
      <Skeleton variant="text" className="w-2/3" />
      <div className="flex gap-2 mt-4">
        <Skeleton variant="button" />
        <Skeleton variant="button" className="w-16" />
      </div>
    </div>
  );
}

export function TemplateCardSkeleton() {
  return (
    <div className="bg-white rounded-xl border border-slate-200 p-6 shadow-lg shadow-slate-200/50">
      <div className="flex items-start justify-between mb-3">
        <Skeleton variant="avatar" />
        <Skeleton variant="button" className="w-16 h-6" />
      </div>
      <Skeleton variant="title" className="mb-2" />
      <Skeleton variant="text" className="mb-1" />
      <Skeleton variant="text" className="w-3/4 mb-4" />
      <div className="flex gap-2">
        <Skeleton variant="button" className="w-20 h-6" />
        <Skeleton variant="button" className="w-16 h-6" />
      </div>
    </div>
  );
}

export function FormSkeleton() {
  return (
    <div className="space-y-6">
      <div>
        <Skeleton variant="title" className="w-1/2 mb-4" />
        <Skeleton variant="text" className="w-3/4 mb-6" />
      </div>
      {[1, 2, 3].map((i) => (
        <div key={i} className="space-y-4">
          <Skeleton variant="title" className="w-1/3" />
          <div className="space-y-4">
            <Skeleton className="h-10 w-full" />
            <Skeleton className="h-10 w-full" />
          </div>
        </div>
      ))}
      <Skeleton variant="button" className="w-32 h-11" />
    </div>
  );
}
