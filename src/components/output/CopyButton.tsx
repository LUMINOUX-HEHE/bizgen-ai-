import { useState } from 'react';
import { Copy, Check } from 'lucide-react';
import { Button } from '@/components/common/Button';
import toast from 'react-hot-toast';

export interface CopyButtonProps {
  text: string;
  variant?: 'primary' | 'secondary' | 'ghost';
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}

export function CopyButton({
  text,
  variant = 'secondary',
  size = 'sm',
  className,
}: CopyButtonProps) {
  const [copied, setCopied] = useState(false);

  const handleCopy = async () => {
    try {
      // Clean the text - remove HTML-like formatting
      const cleanText = text
        .replace(/<[^>]*>/g, '')
        .replace(/\s+/g, ' ')
        .trim();
      
      await navigator.clipboard.writeText(cleanText);
      setCopied(true);
      toast.success('Copied to clipboard!');
      
      setTimeout(() => {
        setCopied(false);
      }, 2000);
    } catch (error) {
      console.error('Failed to copy:', error);
      toast.error('Failed to copy to clipboard');
    }
  };

  return (
    <Button
      variant={variant}
      size={size}
      onClick={handleCopy}
      className={className}
      leftIcon={copied ? <Check className="h-4 w-4" /> : <Copy className="h-4 w-4" />}
    >
      {copied ? 'Copied!' : 'Copy'}
    </Button>
  );
}
