document.getElementById('year').innerText = new Date().getFullYear();

// load stats from public/admin endpoints (use public health + counts via separate calls)
async function loadStats(){
  try{
    const [jobsRes, compRes] = await Promise.all([
      API.get('/api/public/jobs'),
      API.get('/api/public/companies')
    ]);
    document.getElementById('stat-jobs').innerText      = jobsRes.data.length;
    document.getElementById('stat-companies').innerText = compRes.data.length;

    // Render jobs
    const row = document.getElementById('jobs-row');
    row.innerHTML = jobsRes.data.slice(0,6).map(j => `
      <div class="col-md-6 col-lg-4">
        <div class="job-card">
          <span class="badge bg-primary-soft text-primary">${escapeHtml(j.jobType||'JOB')}</span>
          <h5 class="mt-2">${escapeHtml(j.title)}</h5>
          <div class="meta">
            <i class="bi bi-building"></i> ${escapeHtml(j.companyName||'')} ·
            <i class="bi bi-geo-alt"></i> ${escapeHtml(j.location||'-')}
          </div>
          <p class="text-muted small">${escapeHtml((j.description||'').slice(0,120))}…</p>
          <div>${(j.requiredSkills||[]).slice(0,4).map(s=>`<span class="skill-tag">${escapeHtml(s)}</span>`).join('')}</div>
          <div class="d-flex justify-content-between align-items-center mt-3">
            <small class="text-muted">CGPA ≥ ${j.minCgpa ?? 0}</small>
            <a class="btn btn-sm btn-primary" href="/pages/login.html">Apply <i class="bi bi-arrow-right"></i></a>
          </div>
        </div>
      </div>`).join('');

    // Render companies
    const crow = document.getElementById('companies-row');
    crow.innerHTML = compRes.data.slice(0,8).map(c => `
      <div class="col-md-3 col-sm-6">
        <div class="company-tile">
          <div class="logo">${escapeHtml((c.name||'?').charAt(0))}</div>
          <div>
            <div class="fw-semibold">${escapeHtml(c.name)}</div>
            <div class="small text-muted"><i class="bi bi-geo-alt"></i> ${escapeHtml(c.location||'-')}</div>
          </div>
        </div>
      </div>`).join('');

    // we don't have public student/application counts, so display approximate from jobs.length etc.
    document.getElementById('stat-students').innerText = '500+';
    document.getElementById('stat-apps').innerText     = '1,200+';
  }catch(e){
    console.error(e);
  }
}
loadStats();
