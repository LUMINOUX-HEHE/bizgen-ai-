import { useState, useCallback } from 'react';
import { ApiError } from '@/api';

interface UseApiState<T> {
  data: T | null;
  loading: boolean;
  error: ApiError | null;
}

interface UseApiReturn<T, P extends unknown[]> extends UseApiState<T> {
  execute: (...params: P) => Promise<T | null>;
  reset: () => void;
}

export function useApi<T, P extends unknown[] = []>(
  apiFunction: (...params: P) => Promise<T>
): UseApiReturn<T, P> {
  const [state, setState] = useState<UseApiState<T>>({
    data: null,
    loading: false,
    error: null,
  });

  const execute = useCallback(
    async (...params: P): Promise<T | null> => {
      setState((prev) => ({ ...prev, loading: true, error: null }));
      
      try {
        const data = await apiFunction(...params);
        setState({ data, loading: false, error: null });
        return data;
      } catch (error) {
        const apiError = error as ApiError;
        setState({ data: null, loading: false, error: apiError });
        return null;
      }
    },
    [apiFunction]
  );

  const reset = useCallback(() => {
    setState({ data: null, loading: false, error: null });
  }, []);

  return { ...state, execute, reset };
}

export function useApiWithCache<T>(
  apiFunction: () => Promise<T>,
  cacheKey: string,
  cacheDuration: number = 5 * 60 * 1000 // 5 minutes
): UseApiReturn<T, []> {
  const [state, setState] = useState<UseApiState<T>>({
    data: null,
    loading: false,
    error: null,
  });

  const execute = useCallback(async (): Promise<T | null> => {
    // Check cache
    const cached = sessionStorage.getItem(cacheKey);
    if (cached) {
      const { data, timestamp } = JSON.parse(cached);
      if (Date.now() - timestamp < cacheDuration) {
        setState({ data, loading: false, error: null });
        return data;
      }
    }

    setState((prev) => ({ ...prev, loading: true, error: null }));

    try {
      const data = await apiFunction();
      // Store in cache
      sessionStorage.setItem(cacheKey, JSON.stringify({ data, timestamp: Date.now() }));
      setState({ data, loading: false, error: null });
      return data;
    } catch (error) {
      const apiError = error as ApiError;
      setState({ data: null, loading: false, error: apiError });
      return null;
    }
  }, [apiFunction, cacheKey, cacheDuration]);

  const reset = useCallback(() => {
    sessionStorage.removeItem(cacheKey);
    setState({ data: null, loading: false, error: null });
  }, [cacheKey]);

  return { ...state, execute, reset };
}
