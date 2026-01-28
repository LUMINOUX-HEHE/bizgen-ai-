import { cn } from '@/utils/cn';
import { Loader2 } from 'lucide-react';

export interface LoaderProps {
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  text?: string;
}

export function Loader({ size = 'md', className, text }: LoaderProps) {
  const sizes = {
    sm: 'h-4 w-4',
    md: 'h-8 w-8',
    lg: 'h-12 w-12',
  };

  return (
    <div className={cn('flex flex-col items-center justify-center gap-3', className)}>
      <Loader2 className={cn('animate-spin text-blue-600', sizes[size])} />
      {text && <p className="text-sm text-slate-600">{text}</p>}
    </div>
  );
}

export function PageLoader({ text = 'Loading...' }: { text?: string }) {
  return (
    <div className="flex items-center justify-center min-h-[400px]">
      <Loader size="lg" text={text} />
    </div>
  );
}

export function InlineLoader() {
  return <Loader2 className="h-4 w-4 animate-spin text-blue-600" />;
}
