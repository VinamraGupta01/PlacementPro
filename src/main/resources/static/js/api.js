// Tiny API helper used across all pages
const API = (() => {
  const BASE = '';
  const KEY  = 'pp_auth';

  function setAuth(a){ localStorage.setItem(KEY, JSON.stringify(a)); }
  function getAuth(){ try { return JSON.parse(localStorage.getItem(KEY)); } catch(e){ return null; } }
  function clearAuth(){ localStorage.removeItem(KEY); }
  function requireAuth(role){
    const a = getAuth();
    if(!a){ window.location.href='/pages/login.html'; return null; }
    if(role && a.role !== role){
      alert('Access denied for this page. Redirecting…');
      window.location.href = a.role === 'ADMIN' ? '/pages/admin.html' : '/pages/dashboard.html';
      return null;
    }
    return a;
  }

  async function request(path, options={}){
    const auth = getAuth();
    const headers = options.headers || {};
    if(!(options.body instanceof FormData) && options.body){
      headers['Content-Type']='application/json';
    }
    if(auth?.token){ headers['Authorization']='Bearer '+auth.token; }
    const res = await fetch(BASE + path, { ...options, headers });
    const text = await res.text();
    let data; try { data = text ? JSON.parse(text) : {}; } catch(e){ data = { success:false, message:text }; }
    if(!res.ok || data.success === false){
      const msg = data.message || ('HTTP '+res.status);
      throw new Error(msg);
    }
    return data;
  }

  return {
    get  : (p)        => request(p),
    post : (p, body)  => request(p, { method:'POST', body: body instanceof FormData ? body : JSON.stringify(body||{}) }),
    put  : (p, body)  => request(p, { method:'PUT',  body: JSON.stringify(body||{}) }),
    del  : (p)        => request(p, { method:'DELETE' }),
    setAuth, getAuth, clearAuth, requireAuth
  };
})();

function logout(){ API.clearAuth(); window.location.href='/'; }
function fmtDate(d){ if(!d) return '-'; return new Date(d).toLocaleDateString(undefined,{year:'numeric',month:'short',day:'numeric'}); }
function escapeHtml(s){ if(s==null) return ''; return String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'})[c]); }
function toast(msg, type='success'){
  const c = document.createElement('div');
  c.className='alert alert-'+ (type==='error'?'danger':type) +' alert-soft position-fixed top-0 end-0 m-3 shadow';
  c.style.zIndex=9999; c.innerText=msg;
  document.body.appendChild(c);
  setTimeout(()=>c.remove(), 3000);
}
