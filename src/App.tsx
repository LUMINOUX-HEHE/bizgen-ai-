import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Toaster } from 'react-hot-toast';
import { AppLayout } from '@/components/layout/AppLayout';
import { LandingPage } from '@/pages/LandingPage';
import { Dashboard } from '@/pages/Dashboard';
import { CategorySelection } from '@/pages/CategorySelection';
import { TemplateSelection } from '@/pages/TemplateSelection';
import { ContentForm } from '@/pages/ContentForm';
import { GeneratedOutput } from '@/pages/GeneratedOutput';
import { History } from '@/pages/History';
import { NotFound } from '@/pages/NotFound';

export function App() {
  return (
    <Router>
      <Toaster 
        position="top-right"
        toastOptions={{
          duration: 4000,
          style: {
            background: '#1E293B',
            color: '#fff',
            borderRadius: '12px',
            padding: '16px 24px',
            fontSize: '14px',
            fontWeight: '500',
            boxShadow: '0 10px 40px rgba(0, 0, 0, 0.2)',
          },
          success: {
            iconTheme: {
              primary: '#10B981',
              secondary: '#fff',
            },
          },
          error: {
            iconTheme: {
              primary: '#EF4444',
              secondary: '#fff',
            },
          },
        }}
      />
      <Routes>
        <Route path="/" element={<LandingPage />} />
        <Route path="/app" element={<AppLayout />}>
          <Route index element={<Dashboard />} />
          <Route path="create" element={<CategorySelection />} />
          <Route path="create/:categoryId" element={<TemplateSelection />} />
          <Route path="create/:categoryId/:templateId" element={<ContentForm />} />
          <Route path="output/:generationId" element={<GeneratedOutput />} />
          <Route path="history" element={<History />} />
        </Route>
        <Route path="*" element={<NotFound />} />
      </Routes>
    </Router>
  );
}
