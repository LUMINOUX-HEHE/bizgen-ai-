import { useLocation } from 'react-router-dom';

const pageTitles: Record<string, string> = {
  '/': 'Dashboard',
  '/create': 'Create Content',
  '/history': 'History',
};

export function Header() {
  const location = useLocation();
  
  const getTitle = () => {
    if (location.pathname.startsWith('/create/') && location.pathname.split('/').length > 3) {
      return 'Create Content';
    }
    if (location.pathname.startsWith('/output/')) {
      return 'Generated Content';
    }
    return pageTitles[location.pathname] || 'BizGen AI';
  };

  return (
    <header className="sticky top-0 z-30 bg-slate-50/80 backdrop-blur-sm border-b border-slate-200">
      <div className="h-16 px-6 lg:px-8 flex items-center justify-between">
        <h1 className="text-lg font-semibold text-slate-900 lg:text-xl pl-12 lg:pl-0">
          {getTitle()}
        </h1>
        <div className="flex items-center gap-4">
          <div className="h-8 w-8 rounded-full bg-gradient-to-br from-blue-500 to-blue-600 flex items-center justify-center">
            <span className="text-xs font-medium text-white">BG</span>
          </div>
        </div>
      </div>
    </header>
  );
}
