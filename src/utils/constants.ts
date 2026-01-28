export const COLORS = {
  primary: '#2563EB',
  secondary: '#0F172A',
  accent: '#10B981',
  warning: '#F59E0B',
  error: '#EF4444',
  background: '#F8FAFC',
  cardBg: '#FFFFFF',
  textPrimary: '#1E293B',
  textSecondary: '#64748B',
};

export const DIFFICULTY_COLORS = {
  EASY: 'bg-emerald-100 text-emerald-700',
  MEDIUM: 'bg-amber-100 text-amber-700',
  ADVANCED: 'bg-red-100 text-red-700',
};

export const DIFFICULTY_LABELS = {
  EASY: 'Easy',
  MEDIUM: 'Medium',
  ADVANCED: 'Advanced',
};

export const ICON_MAP: Record<string, string> = {
  megaphone: 'megaphone',
  shield: 'shield',
  file: 'file',
  mail: 'mail',
  chart: 'chart',
};

export const STATUS_COLORS = {
  PENDING: 'bg-yellow-100 text-yellow-700',
  COMPLETED: 'bg-green-100 text-green-700',
  FAILED: 'bg-red-100 text-red-700',
};

export const LOCAL_STORAGE_KEYS = {
  DRAFT_PREFIX: 'bizgen_draft_',
};

export const API_ENDPOINTS = {
  CATEGORIES: '/categories',
  TEMPLATES: '/templates',
  GENERATE: '/generate',
  HISTORY: '/history',
  HEALTH: '/health',
};

export const DEFAULT_PAGE_SIZE = 20;
export const MAX_PAGE_SIZE = 100;
