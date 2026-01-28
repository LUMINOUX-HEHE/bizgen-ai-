import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Card } from '@/components/common/Card';
import { Button } from '@/components/common/Button';
import { Badge } from '@/components/common/Badge';
import { PageLoader } from '@/components/common/Loader';
import { EmptyState } from '@/components/common/EmptyState';
import { fetchGenerationById, fetchHistoryItemById, GenerationResponse, Variation } from '@/api';
import { formatDate, formatDuration } from '@/utils/formatters';
import { 
  ArrowLeft, 
  CheckCircle, 
  AlertTriangle, 
  AlertCircle, 
  Copy, 
  Check, 
  ChevronLeft, 
  ChevronRight,
  Home,
  PlusCircle,
  Clock
} from 'lucide-react';
import toast from 'react-hot-toast';

export function GeneratedOutput() {
  const { generationId } = useParams<{ generationId: string }>();
  const navigate = useNavigate();
  const [generation, setGeneration] = useState<GenerationResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [activeVariation, setActiveVariation] = useState(0);
  const [copiedId, setCopiedId] = useState<string | null>(null);

  const loadGeneration = async (id: string) => {
    setLoading(true);
    try {
      // Try fetchGenerationById first, then fetchHistoryItemById as fallback
      let data: GenerationResponse;
      try {
        data = await fetchGenerationById(id);
      } catch {
        data = await fetchHistoryItemById(id);
      }
      setGeneration(data);
    } catch (error) {
      console.error('Failed to load generation:', error);
      toast.error('Failed to load generated content.');
      navigate('/history');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (generationId) {
      loadGeneration(generationId);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [generationId]);

  const goToPrevious = useCallback(() => {
    if (!generation) return;
    setActiveVariation((prev) => 
      prev > 0 ? prev - 1 : generation.variations.length - 1
    );
  }, [generation]);

  const goToNext = useCallback(() => {
    if (!generation) return;
    setActiveVariation((prev) => 
      prev < generation.variations.length - 1 ? prev + 1 : 0
    );
  }, [generation]);

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

  const copyToClipboard = async (variation: Variation) => {
    try {
      // Clean the text - remove HTML-like formatting
      const cleanText = variation.content
        .replace(/<[^>]*>/g, '')
        .replace(/\s+/g, ' ')
        .trim();
      
      await navigator.clipboard.writeText(cleanText);
      setCopiedId(variation.id);
      toast.success('Copied to clipboard!');
      
      setTimeout(() => {
        setCopiedId(null);
      }, 2000);
    } catch (error) {
      console.error('Failed to copy:', error);
      toast.error('Failed to copy to clipboard');
    }
  };

  const highlightPlaceholders = (content: string) => {
    return content.replace(
      /\[([A-Z][A-Z_0-9]*)\]/g,
      '<span class="bg-amber-100 text-amber-800 px-1 rounded font-medium">[$1]</span>'
    );
  };

  if (loading) {
    return <PageLoader text="Loading generated content..." />;
  }

  if (!generation) {
    return (
      <EmptyState
        icon="file"
        title="Content Not Found (404)"
        description={
          <>
            The generated content you're looking for doesn't exist.<br />
            <span className="block mt-2 text-xs text-slate-400">
              <b>Troubleshooting:</b><br />
              • Make sure you generated content and are using a valid link.<br />
              • If you just generated content and see this, your backend may not be saving generations.<br />
              • If you see this for all generations, check backend logs and API connection.<br />
              • If you just fixed your API key, try generating new content.<br />
            </span>
          </>
        }
        actionLabel="Go to History"
        onAction={() => navigate('/history')}
      />
    );
  }

  const currentVariation = generation.variations?.[activeVariation];
  const hasDisclaimers = generation.disclaimers && generation.disclaimers.length > 0;
  const hasWarnings = generation.warnings && generation.warnings.length > 0;

  // Guard for missing variations
  if (!generation.variations || generation.variations.length === 0 || !currentVariation) {
    return (
      <EmptyState
        icon="file"
        title="No content variations"
        description="This generation doesn't have any content variations."
        actionLabel="Go to History"
        onAction={() => navigate('/history')}
      />
    );
  }

  return (
    <div className="space-y-6 max-w-4xl mx-auto">
      {/* Back Button */}
      <Button
        variant="ghost"
        size="sm"
        onClick={() => navigate(-1)}
        leftIcon={<ArrowLeft className="h-4 w-4" />}
      >
        Back
      </Button>

      {/* Success Header */}
      <Card className="bg-gradient-to-r from-emerald-50 to-teal-50 border-emerald-200">
        <div className="flex items-start gap-4">
          <div className="h-12 w-12 rounded-full bg-emerald-100 flex items-center justify-center flex-shrink-0">
            <CheckCircle className="h-6 w-6 text-emerald-600" />
          </div>
          <div className="flex-1">
            <h1 className="text-xl font-bold text-slate-900 mb-1">
              Content Generated Successfully
            </h1>
            <p className="text-slate-600">
              Template: <span className="font-medium">{generation.templateName}</span>
            </p>
            <div className="flex items-center gap-4 mt-2 text-sm text-slate-500">
              <span>{generation.category}</span>
              <span className="flex items-center gap-1">
                <Clock className="h-4 w-4" />
                {formatDuration(generation.generationTimeMs)}
              </span>
              <span>{formatDate(generation.createdAt)}</span>
            </div>
          </div>
          <Badge variant="success">
            {generation.variations.length} variations
          </Badge>
        </div>
      </Card>

      {/* Disclaimers */}
      {hasDisclaimers && (
        <div className="bg-amber-50 border border-amber-200 rounded-lg p-4">
          <div className="flex items-start gap-3">
            <AlertTriangle className="h-5 w-5 text-amber-600 flex-shrink-0 mt-0.5" />
            <div>
              <h4 className="font-semibold text-amber-800 mb-2">Important Notice</h4>
              <ul className="space-y-1">
                {generation.disclaimers.map((disclaimer, index) => (
                  <li key={index} className="text-sm text-amber-700">
                    • {disclaimer}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      )}

      {/* Warnings */}
      {hasWarnings && (
        <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
          <div className="flex items-start gap-3">
            <AlertCircle className="h-5 w-5 text-blue-600 flex-shrink-0 mt-0.5" />
            <div>
              <h4 className="font-semibold text-blue-800 mb-2">
                Review Notes ({generation.warnings.length})
              </h4>
              <ul className="space-y-1">
                {generation.warnings.map((warning, index) => (
                  <li key={index} className="text-sm text-blue-700">
                    • {warning}
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      )}

      {/* Variation Display */}
      <Card padding="lg">
        <div className="flex items-center justify-between mb-4">
          <Badge variant="primary">
            Variation {currentVariation.variationNumber} of {generation.variations.length}
          </Badge>
          <Button
            variant="secondary"
            size="sm"
            onClick={() => copyToClipboard(currentVariation)}
            leftIcon={copiedId === currentVariation.id ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
          >
            {copiedId === currentVariation.id ? 'Copied!' : 'Copy'}
          </Button>
        </div>

        {/* Placeholders Notice */}
        {currentVariation.placeholders && currentVariation.placeholders.length > 0 && (
          <div className="mb-4 flex flex-wrap gap-2">
            <span className="text-xs text-slate-500">Needs attention:</span>
            {currentVariation.placeholders.map((placeholder, index) => (
              <Badge key={index} variant="warning" size="sm">
                {placeholder.replace(/_/g, ' ').toLowerCase()}
              </Badge>
            ))}
          </div>
        )}

        {/* Content */}
        <div
          className="prose prose-slate max-w-none text-sm leading-relaxed whitespace-pre-wrap bg-slate-50 rounded-lg p-6 min-h-[200px]"
          dangerouslySetInnerHTML={{
            __html: highlightPlaceholders(currentVariation.content),
          }}
        />
      </Card>

      {/* Navigation */}
      {generation.variations.length > 1 && (
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
            {generation.variations.map((_, index) => (
              <button
                key={index}
                onClick={() => setActiveVariation(index)}
                className={`w-2.5 h-2.5 rounded-full transition-colors ${
                  index === activeVariation
                    ? 'bg-blue-600'
                    : 'bg-slate-300 hover:bg-slate-400'
                }`}
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
      {generation.variations.length > 1 && (
        <p className="text-center text-xs text-slate-400">
          Use ← → arrow keys to navigate between variations
        </p>
      )}

      {/* Actions */}
      <Card>
        <div className="flex items-center justify-between">
          <div className="text-sm text-slate-500">
            Generation ID: <code className="text-xs bg-slate-100 px-1 rounded">{generation.generationId || generationId}</code>
          </div>
          <div className="flex items-center gap-3">
            <Button
              variant="secondary"
              onClick={() => navigate('/')}
              leftIcon={<Home className="h-4 w-4" />}
            >
              Dashboard
            </Button>
            <Button
              onClick={() => navigate('/create')}
              leftIcon={<PlusCircle className="h-4 w-4" />}
            >
              Create Another
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
}
