import { useState, useCallback } from 'react';
import toast from 'react-hot-toast';

interface UseClipboardReturn {
  copied: boolean;
  copy: (text: string) => Promise<void>;
}

export function useClipboard(resetDelay: number = 2000): UseClipboardReturn {
  const [copied, setCopied] = useState(false);

  const copy = useCallback(async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      setCopied(true);
      toast.success('Copied to clipboard!');
      
      setTimeout(() => {
        setCopied(false);
      }, resetDelay);
    } catch (error) {
      console.error('Failed to copy:', error);
      toast.error('Failed to copy to clipboard');
    }
  }, [resetDelay]);

  return { copied, copy };
}
