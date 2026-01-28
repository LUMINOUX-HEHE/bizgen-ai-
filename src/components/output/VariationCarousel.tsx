import { useState, useEffect, useCallback } from 'react';
import { Variation } from '@/api';
import { VariationCard } from './VariationCard';
import { Button } from '@/components/common/Button';
import { ChevronLeft, ChevronRight } from 'lucide-react';
import { cn } from '@/utils/cn';

export interface VariationCarouselProps {
  variations: Variation[];
  className?: string;
}

export function VariationCarousel({
  variations,
  className,
}: VariationCarouselProps) {
  const [activeIndex, setActiveIndex] = useState(0);

  const goToPrevious = useCallback(() => {
    setActiveIndex((prev) => (prev > 0 ? prev - 1 : variations.length - 1));
  }, [variations.length]);

  const goToNext = useCallback(() => {
    setActiveIndex((prev) => (prev < variations.length - 1 ? prev + 1 : 0));
  }, [variations.length]);

  // Keyboard navigation
  useEffect(() => {
    const handleKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'ArrowLeft') {
        goToPrevious();
      } else if (e.key === 'ArrowRight') {
        goToNext();
      }
    };

    window.addEventListener('keydown', handleKeyDown);
    return () => window.removeEventListener('keydown', handleKeyDown);
  }, [goToPrevious, goToNext]);

  if (variations.length === 0) {
    return null;
  }

  return (
    <div className={cn('space-y-4', className)}>
      {/* Active variation */}
      <VariationCard variation={variations[activeIndex]} isActive={true} />

      {/* Navigation */}
      {variations.length > 1 && (
        <div className="flex items-center justify-between">
          <Button
            variant="secondary"
            size="sm"
            onClick={goToPrevious}
            leftIcon={<ChevronLeft className="h-4 w-4" />}
          >
            Previous
          </Button>

          {/* Dot indicators */}
          <div className="flex items-center gap-2">
            {variations.map((_, index) => (
              <button
                key={index}
                onClick={() => setActiveIndex(index)}
                className={cn(
                  'w-2.5 h-2.5 rounded-full transition-colors',
                  index === activeIndex
                    ? 'bg-blue-600'
                    : 'bg-slate-300 hover:bg-slate-400'
                )}
                aria-label={`Go to variation ${index + 1}`}
              />
            ))}
          </div>

          <Button
            variant="secondary"
            size="sm"
            onClick={goToNext}
            rightIcon={<ChevronRight className="h-4 w-4" />}
          >
            Next
          </Button>
        </div>
      )}

      {/* Keyboard hint */}
      <p className="text-center text-xs text-slate-400">
        Use ← → arrow keys to navigate between variations
      </p>
    </div>
  );
}
