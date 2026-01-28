import axios, { AxiosError, AxiosInstance } from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api/v1';

const api: AxiosInstance = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 30000,
});

// Request interceptor
api.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    const errorResponse = error.response?.data as ApiError | undefined;
    if (errorResponse) {
      return Promise.reject(errorResponse);
    }
    return Promise.reject({
      code: 'NETWORK_ERROR',
      message: error.message || 'Network error occurred. Please check your connection.',
      timestamp: new Date().toISOString(),
    });
  }
);

// Types
export interface ApiError {
  code: string;
  message: string;
  errors?: FieldError[];
  timestamp: string;
}

export interface FieldError {
  field: string;
  message: string;
  rejectedValue?: unknown;
}

export interface Category {
  id: string;
  name: string;
  displayName: string;
  description: string;
  icon: string;
  templateCount: number;
  requiresDisclaimer: boolean;
}

export interface Template {
  id: string;
  name: string;
  description: string;
  categoryId: string;
  categoryName: string;
  estimatedTime: string;
  difficulty: 'EASY' | 'MEDIUM' | 'ADVANCED';
  popular: boolean;
}

export interface FieldValidation {
  minLength?: number;
  maxLength?: number;
  pattern?: string;
  min?: number;
  max?: number;
}

export interface SelectOption {
  value: string;
  label: string;
}

export interface FormField {
  id: string;
  name: string;
  label: string;
  type: 'text' | 'textarea' | 'select' | 'multiselect' | 'radio' | 'checkbox';
  placeholder?: string;
  helpText?: string;
  required: boolean;
  validation?: FieldValidation;
  options?: SelectOption[];
  defaultValue?: string | string[] | boolean;
  maxLength?: number;
  rows?: number;
}

export interface FormSection {
  id: string;
  title: string;
  description?: string;
  order: number;
  fields: FormField[];
}

export interface TemplateSchema {
  templateId: string;
  templateName: string;
  description: string;
  category: string;
  estimatedTime: string;
  sections: FormSection[];
}

export interface Variation {
  id: string;
  variationNumber: number;
  content: string;
  placeholders: string[];
}

export interface GenerationResponse {
  generationId: string;
  templateId: string;
  templateName: string;
  category: string;
  variations: Variation[];
  disclaimers: string[];
  warnings: string[];
  generationTimeMs: number;
  createdAt: string;
}

// Alias for when fetching from history - the id field may be different
export type GenerationFromHistory = GenerationResponse;

export interface HistoryItem {
  id: string;
  templateId: string;
  templateName: string;
  categoryId: string;
  categoryName: string;
  status: string;
  generationTimeMs: number;
  variationCount: number;
  createdAt: string;
}

export interface Warning {
  type: string;
  message: string;
  severity: string;
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

export interface HealthResponse {
  status: string;
  version: string;
  timestamp: string;
  components: {
    database: string;
    aiService: string;
  };
}

export interface GenerateRequest {
  templateId: string;
  inputs: Record<string, unknown>;
  options?: {
    tone?: string;
    length?: 'short' | 'medium' | 'long';
    variationCount?: number;
  };
}

// API Functions

// Categories
export const fetchCategories = async (): Promise<Category[]> => {
  const response = await api.get<Category[]>('/categories');
  return response.data;
};

export const fetchCategoryById = async (id: string): Promise<Category> => {
  const response = await api.get<Category>(`/categories/${id}`);
  return response.data;
};

// Templates
export const fetchTemplates = async (categoryId?: string): Promise<Template[]> => {
  const params = categoryId ? { categoryId } : {};
  const response = await api.get<Template[]>('/templates', { params });
  return response.data;
};

export const fetchTemplateById = async (id: string): Promise<Template> => {
  const response = await api.get<Template>(`/templates/${id}`);
  return response.data;
};

export const fetchTemplateSchema = async (id: string): Promise<TemplateSchema> => {
  const response = await api.get<TemplateSchema>(`/templates/${id}/schema`);
  return response.data;
};

export const fetchPopularTemplates = async (): Promise<Template[]> => {
  try {
    const response = await api.get<Template[]>('/templates/popular');
    return response.data;
  } catch {
    // Fallback: fetch all templates and filter popular ones
    const allTemplates = await fetchTemplates();
    return allTemplates.filter(t => t.popular);
  }
};

// Generation
export const generateContent = async (request: GenerateRequest): Promise<GenerationResponse> => {
  const response = await api.post<GenerationResponse>('/generate', request);
  return response.data;
};

export const fetchGenerationById = async (id: string): Promise<GenerationResponse> => {
  const response = await api.get<GenerationResponse>(`/generate/${id}`);
  return response.data;
};

// History
export const fetchHistory = async (
  page: number = 0,
  size: number = 20,
  filters?: {
    categoryId?: string;
    templateId?: string;
    fromDate?: string;
    toDate?: string;
  }
): Promise<PageResponse<HistoryItem>> => {
  const params = { page, size, ...filters };
  const response = await api.get<PageResponse<HistoryItem>>('/history', { params });
  return response.data;
};

export const fetchHistoryItemById = async (id: string): Promise<GenerationResponse> => {
  const response = await api.get<GenerationResponse>(`/history/${id}`);
  return response.data;
};

export const deleteHistoryItem = async (id: string): Promise<void> => {
  await api.delete(`/history/${id}`);
};

// Health
export const checkHealth = async (): Promise<HealthResponse> => {
  const response = await api.get<HealthResponse>('/health');
  return response.data;
};

export default api;
