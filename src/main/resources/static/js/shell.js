// Renders the sidebar + topbar for student/admin dashboards
function renderShell(active){
  const auth = API.getAuth();
  if(!auth){ window.location.href='/pages/login.html'; return; }

  const isAdmin = auth.role === 'ADMIN';
  const links = isAdmin ? [
      { id:'admin',        href:'/pages/admin.html',         icon:'speedometer2', label:'Dashboard'   },
      { id:'admin-jobs',   href:'/pages/admin-jobs.html',    icon:'briefcase',    label:'Jobs'        },
      { id:'admin-companies',href:'/pages/admin-companies.html',icon:'buildings',label:'Companies'   },
      { id:'admin-apps',   href:'/pages/admin-apps.html',    icon:'inbox',        label:'Applications'},
  ] : [
      { id:'dashboard',    href:'/pages/dashboard.html',     icon:'house',        label:'Dashboard'   },
      { id:'jobs',         href:'/pages/jobs.html',          icon:'briefcase',    label:'Browse Jobs' },
      { id:'applications', href:'/pages/applications.html',  icon:'inbox',        label:'My Applications'},
      { id:'profile',      href:'/pages/profile.html',       icon:'person',       label:'Profile'     },
  ];

  const initial = (auth.name||auth.email||'?').charAt(0).toUpperCase();

  document.body.innerHTML = `
    <div class="app-shell">
      <aside class="sidebar">
        <div class="brand">
          <span class="brand-logo">P</span> PlacementPro
        </div>
        <div class="small-h">${isAdmin?'Admin':'Student'}</div>
        ${links.map(l=>`<a href="${l.href}" class="${l.id===active?'active':''}"><i class="bi bi-${l.icon}"></i> ${l.label}</a>`).join('')}
        <div class="small-h mt-4">Resources</div>
        <a href="/swagger-ui.html" target="_blank"><i class="bi bi-braces"></i> API Docs</a>
        <a href="/"><i class="bi bi-house-door"></i> Public site</a>
        <a href="#" onclick="logout();return false;"><i class="bi bi-box-arrow-right"></i> Logout</a>
      </aside>
      <main class="content">
        <div class="topbar">
          <div>
            <h4 class="fw-bold mb-0" id="page-title"></h4>
            <div class="text-muted small" id="page-sub"></div>
          </div>
          <div class="d-flex align-items-center gap-3">
            <div class="text-end d-none d-sm-block">
              <div class="fw-semibold">${escapeHtml(auth.name||auth.email)}</div>
              <div class="small text-muted">${auth.role}</div>
            </div>
            <div class="brand-logo" style="width:42px;height:42px;font-size:1rem;">${escapeHtml(initial)}</div>
          </div>
        </div>
        <div id="page-body"></div>
      </main>
    </div>`;
  return auth;
}

function setTitle(title, sub){
  document.getElementById('page-title').innerText = title;
  document.getElementById('page-sub').innerText   = sub||'';
}
function setBody(html){ document.getElementById('page-body').innerHTML = html; }
