-- Categories
MERGE INTO categories (id, name, display_name, description, icon, display_order, active, requires_disclaimer, created_at, updated_at) KEY(id)
VALUES
('cat-marketing', 'marketing', 'Marketing Content', 'Social media posts, email campaigns, and promotional content for your business', 'megaphone', 1, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('cat-legal', 'legal', 'Legal Documents', 'Privacy policies, terms of service, and compliance document drafts', 'shield', 2, true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Marketing Templates
MERGE INTO templates (id, category_id, name, description, blueprint_path, estimated_time, display_order, active, popular, difficulty, created_at, updated_at) KEY(id)
VALUES
('tpl-instagram', 'cat-marketing', 'Instagram Post', 'Create engaging Instagram posts that capture attention and drive engagement', 'marketing/instagram-post.json', '2-3 minutes', 1, true, true, 'EASY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tpl-facebook', 'cat-marketing', 'Facebook Post', 'Professional Facebook posts for your business page', 'marketing/facebook-post.json', '2-3 minutes', 2, true, false, 'EASY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tpl-linkedin', 'cat-marketing', 'LinkedIn Post', 'Professional thought leadership content for LinkedIn', 'marketing/linkedin-post.json', '3-4 minutes', 3, true, false, 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tpl-twitter', 'cat-marketing', 'Twitter Post', 'Concise and impactful tweets for your brand', 'marketing/twitter-post.json', '1-2 minutes', 4, true, false, 'EASY', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tpl-email', 'cat-marketing', 'Email Newsletter', 'Engaging email newsletters for your subscriber list', 'marketing/email-newsletter.json', '5-7 minutes', 5, true, true, 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Legal Templates
MERGE INTO templates (id, category_id, name, description, blueprint_path, estimated_time, display_order, active, popular, difficulty, created_at, updated_at) KEY(id)
VALUES
('tpl-privacy', 'cat-legal', 'Privacy Policy', 'Generate a comprehensive privacy policy draft for your website or app', 'legal/privacy-policy.json', '5-8 minutes', 1, true, true, 'MEDIUM', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('tpl-terms', 'cat-legal', 'Terms of Service', 'Create terms of service draft covering key legal provisions', 'legal/terms-of-service.json', '8-10 minutes', 2, true, false, 'ADVANCED', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
