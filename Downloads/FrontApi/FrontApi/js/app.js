const API_URL = "http://localhost:8080/api";

// ==========================================
// 1. Utilidades y Autenticación
// ==========================================
function getToken() {
    return localStorage.getItem("token") || sessionStorage.getItem("token") || "";
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("rol");
    localStorage.removeItem("username");
    window.location.href = "login.html";
}

// Interceptor básico de Fetch para incluir el JWT en todas las peticiones
async function fetchAPI(endpoint, options = {}) {
    const token = getToken();
    const headers = {
        "Content-Type": "application/json",
        ...(token ? { "Authorization": `Bearer ${token}` } : {})
    };
    const config = { ...options, headers: { ...headers, ...options.headers } };

    try {
        const res = await fetch(`${API_URL}${endpoint}`, config);
        
        if (res.status === 401 || res.status === 403) {
            logout(); // Si el token expira, enviarlo al login
            return;
        }
        
        if (!res.ok) {
            const err = await res.json().catch(() => ({}));
            throw new Error(err.mensaje || err.message || "Error en la petición al servidor");
        }
        
        if (res.status === 204) return null; // No content (como en el DELETE)
        return await res.json();
    } catch (error) {
        console.error("API Error:", error);
        throw error;
    }
}

// Convertidor de archivos a Base64 para firmas
function fileToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
    });
}

// ==========================================
// 2. Inicializador Principal (Router básico)
// ==========================================
document.addEventListener("DOMContentLoaded", () => {
    const path = window.location.pathname.toLowerCase();

    // Redirigir si no hay token y no es login
    if (!getToken() && !path.includes("login.html")) {
        window.location.href = "login.html";
        return;
    }

    // Inyectar nombre de usuario dinámico en los headers y barras laterales
    const usernameDisplay = document.getElementById("username-display");
    const sidebarUsername = document.getElementById("sidebar-username");
    const welcomeName = document.getElementById("welcome-name");
    
    const username = localStorage.getItem("username") || "Usuario";
    
    if (usernameDisplay) usernameDisplay.textContent = username;
    if (sidebarUsername) sidebarUsername.textContent = username;
    if (welcomeName) welcomeName.textContent = username;

    // Identificar la página actual y ejecutar su lógica respectiva
    if (path.includes("dashboard.html")) initDashboard();
    else if (path.includes("donantes.html")) initDonantes();
    else if (path.includes("donaciones.html")) initDonaciones();
    else if (path.includes("consentimientos.html") || path.includes("consentimiento.html")) initConsentimientos();
    else if (path.includes("inventario.html")) initInventario();

    // Iniciar controladores globales de UI
    setupModals();
    setupCanvas();
});

// ==========================================
// 3. Lógica por Página
// ==========================================

/* --- DASHBOARD --- */
async function initDashboard() {
    try {
        const [donantesRes, donacionesRes, invRes] = await Promise.all([
            fetchAPI("/donantes?size=1"), 
            fetchAPI("/donaciones?size=5&sort=fechaDonacion,desc"),
            fetchAPI("/inventario")
        ]);

        const totalDonantes = donantesRes.totalElements || 0;
        const totalDonaciones = donacionesRes.totalElements || 0;
        const litros = (totalDonaciones * 450) / 1000;

        const mcValues = document.querySelectorAll(".mc-value");
        if (mcValues.length >= 3) {
            mcValues[0].textContent = totalDonantes;
            mcValues[1].textContent = totalDonaciones;
            mcValues[2].textContent = `${litros.toFixed(1)} L`;
        }

        renderInventario(invRes, document.querySelector('.inventory-list'));

        const tbody = document.querySelector(".data-table tbody");
        if (tbody && donacionesRes.content) {
            tbody.innerHTML = donacionesRes.content.map(d => `
                <tr>
                    <td>
                        <div class="donor-name">${d.nombreDonante}</div>
                    </td>
                    <td><span class="blood-chip">${d.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-')}</span></td>
                    <td>${d.cantidadMl} mL</td>
                    <td>${new Date(d.fechaDonacion).toLocaleDateString()}</td>
                    <td><span class="status-pill ok">${d.estado}</span></td>
                </tr>
            `).join("");
        }
    } catch (error) {
        console.error("Error cargando el dashboard:", error);
    }
}

/* --- DONANTES --- */
async function initDonantes(page = 0) {
    try {
        const response = await fetchAPI(`/donantes?page=${page}&size=8&sort=nombres,asc`);
        const tbody = document.getElementById("donantesBody");
        const countLabel = document.getElementById("countLabel");
        const pageInfo = document.getElementById("pageInfo");

        if (tbody) {
            if (response.content.length === 0) {
                tbody.innerHTML = '<tr class="empty-row"><td colspan="8" style="text-align: center; padding: 30px;">No hay donantes registrados.</td></tr>';
            } else {
                if (countLabel) countLabel.textContent = `${response.totalElements} registros`;
                if (pageInfo) pageInfo.textContent = `Página ${response.pageable.pageNumber + 1} de ${response.totalPages}`;

                tbody.innerHTML = response.content.map(d => `
                    <tr>
                        <td>
                            <div class="donor-cell">
                                <div class="donor-avatar">${d.nombres.charAt(0)}${d.apellidos.charAt(0)}</div>
                                <div class="donor-cell-info">
                                    <div class="name">${d.nombres} ${d.apellidos}</div>
                                    <div class="doc">${d.documento}</div>
                                </div>
                            </div>
                        </td>
                        <td><span class="blood-chip">${d.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-')}</span></td>
                        <td>${d.peso} kg</td>
                        <td>${d.telefono}</td>
                        <td>${d.fechaUltimaDonacion || 'N/A'}</td>
                        <td>${d.aceptaConsentimiento ? '<span class="status-pill ok">Firmado</span>' : '<span class="status-pill pending">Pendiente</span>'}</td>
                        <td><span class="status-pill ${d.activo ? 'ok' : 'blocked'}">${d.activo ? 'Activo' : 'Inactivo'}</span></td>
                        <td>
                            <div class="actions-cell">
                                <button class="action-btn" onclick="eliminarDonante(${d.id})" title="Desactivar"><i class="fa-solid fa-trash"></i></button>
                            </div>
                        </td>
                    </tr>
                `).join("");
            }
        }

        const form = document.getElementById("consentimientoForm");
        if (form) {
            form.onsubmit = async (e) => {
                e.preventDefault();
                await guardarDonanteConFirma();
            };
        }
    } 
    catch (error) {
        console.error("Error cargando donantes:", error);
    }
}

async function eliminarDonante(id) {
    if (confirm("¿Seguro que deseas desactivar este donante?")) {
        try {
            await fetchAPI(`/donantes/${id}`, { method: 'DELETE' });
            alert("Donante desactivado.");
            initDonantes(0); // Refrescar tabla
        } catch (error) {
            alert("Error: " + error.message);
        }
    }
}


/* --- INVENTARIO --- */
async function initInventario() {
    try {
        const invRes = await fetchAPI("/inventario");

        // Sumar y actualizar el total global
        const totalGlobal = invRes.reduce((acc, curr) => acc + curr.cantidadDisponibleMl, 0);
        const totalLiters = (totalGlobal / 1000).toFixed(1);
        const globalLabel = document.getElementById("totalGlobalVolume");
        if (globalLabel) globalLabel.textContent = `${totalLiters} L`;

        renderInventario(invRes, document.querySelector('.inventory-list') || document.querySelector('#inventoryGridContainer'));
    } catch (error) {
        console.error("Error cargando inventario:", error);
    }
}

function renderInventario(data, containerElement) {
    if(!containerElement) return;

    const MAX_ML = 50000;
    // Identificar si renderizamos para dashboard (mini) o para la grid full de inventario.html
    const isGrid = containerElement.id === 'inventoryGridContainer';

    containerElement.innerHTML = data.map(item => {
        const pct = Math.min(Math.round((item.cantidadDisponibleMl / MAX_ML) * 100), 100);
        const cls = pct < 20 ? 'warning' : 'ok';
        const cardCls = pct < 20 ? 'low-stock' : '';
        const statusText = pct < 20 ? 'Crítico' : 'Óptimo';
        const statusCls = pct < 20 ? 'status-critico' : 'status-optimo';
        const tipoLimpio = item.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-');
        
        if (isGrid) {
            return `
            <div class="blood-card ${cardCls}">
                <div class="bc-header">
                    <div class="bc-type">${tipoLimpio}</div>
                    <div class="bc-status ${statusCls}">${statusText}</div>
                </div>
                <div class="bc-info-row">
                    <span>Volumen disponible</span>
                    <strong>${(item.cantidadDisponibleMl / 1000).toFixed(1)} L</strong>
                </div>
                <div class="progress-container">
                    <div class="progress-label">
                        <span>Capacidad (${pct}%)</span>
                        <span>${item.cantidadDisponibleMl} ml / ${MAX_ML} ml</span>
                    </div>
                    <div class="progress-bar-bg">
                        <div class="progress-bar-fill ${cls}" style="width: ${pct}%;"></div>
                    </div>
                </div>
            </div>`;
        } else {
            // Dashboard format
            const dashCls = pct < 20 ? 'low' : (pct < 50 ? 'med' : 'ok');
            return `
            <div class="inv-item" style="display:flex; align-items:center; gap:10px; margin-bottom:10px;">
                <div class="inv-type" style="font-weight:bold; color:#dc2626; width:30px;">${tipoLimpio}</div>
                <div class="inv-bar-wrap" style="flex-grow:1;">
                    <div class="inv-bar-row" style="display:flex; justify-content:space-between; font-size:0.75rem; color:#64748b;">
                        <span>${item.cantidadDisponibleMl} mL</span>
                        <span>${pct}%</span>
                    </div>
                    <div class="inv-bar-bg" style="height:6px; background:#e2e8f0; border-radius:3px; overflow:hidden;">
                        <div class="inv-bar-fill ${dashCls}" style="height:100%; width:${pct}%; background:${pct < 20 ? '#ef4444' : '#10b981'}"></div>
                    </div>
                </div>
            </div>`;
        }
    }).join("");
}

// ==========================================
// 4. Funciones Reutilizables y Helpers UI
// ==========================================

function setupModals() {
    window.openModal = function(id) {
        const m = document.getElementById(id);
        if(m) {
            m.classList.add('open');
            document.body.style.overflow = 'hidden';
        }
    }
    window.closeModal = function(id) {
        const m = document.getElementById(id);
        if(m) {
            m.classList.remove('open');
            document.body.style.overflow = '';
        }
    }
    window.closeOnBg = function(e, id) {
        if (e.target === document.getElementById(id)) closeModal(id);
    }
    document.addEventListener('keydown', e => {
        if (e.key === 'Escape') {
            document.querySelectorAll('.modal-overlay.open').forEach(m => {
                m.classList.remove('open');
                document.body.style.overflow = '';
            });
        }
    });
}

function setupCanvas() {
    const canvas = document.getElementById("canvasFirma");
    const btnClearCanvas = document.getElementById("btnClearCanvas");
    if (!canvas) return;

    const ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#0f172a"; 
    ctx.lineWidth = 2.5;
    ctx.lineCap = "round";
    let dibujando = false;

    function getPos(e) {
        const rect = canvas.getBoundingClientRect();
        const clientX = e.touches ? e.touches[0].clientX : e.clientX;
        const clientY = e.touches ? e.touches[0].clientY : e.clientY;
        return { x: clientX - rect.left, y: clientY - rect.top };
    }

    // Eventos para mouse
    canvas.addEventListener("mousedown", (e) => { dibujando = true; const pos = getPos(e); ctx.beginPath(); ctx.moveTo(pos.x, pos.y); });
    canvas.addEventListener("mousemove", (e) => { if (dibujando) { const pos = getPos(e); ctx.lineTo(pos.x, pos.y); ctx.stroke(); }});
    canvas.addEventListener("mouseup", () => dibujando = false);
    canvas.addEventListener("mouseleave", () => dibujando = false);

    // Eventos táctiles para móviles/tablets
    canvas.addEventListener("touchstart", (e) => { e.preventDefault(); dibujando = true; const pos = getPos(e); ctx.beginPath(); ctx.moveTo(pos.x, pos.y); }, {passive: false});
    canvas.addEventListener("touchmove", (e) => { e.preventDefault(); if (dibujando) { const pos = getPos(e); ctx.lineTo(pos.x, pos.y); ctx.stroke(); }}, {passive: false});
    canvas.addEventListener("touchend", () => dibujando = false);

    if(btnClearCanvas) {
        btnClearCanvas.addEventListener("click", () => {
            ctx.clearRect(0, 0, canvas.width, canvas.height);
        });
    }
}

// ==========================================
// 5. Gestión de Firma y Creación de Donante
// ==========================================

let modoFirmaActual = 'dibujar';

window.setModoFirma = function(modo) {
    modoFirmaActual = modo;
    const btnDibujar = document.getElementById("btnFirmaDibujar");
    const btnSubir = document.getElementById("btnFirmaSubir");
    const wrapCanvas = document.getElementById("wrapperCanvas");
    const wrapArchivo = document.getElementById("wrapperArchivo");

    if (!btnDibujar || !btnSubir) return;

    if(modo === 'dibujar') {
        btnDibujar.classList.add("active");
        btnSubir.classList.remove("active");
        wrapCanvas.style.display = "block";
        wrapArchivo.style.display = "none";
    } else {
        btnDibujar.classList.remove("active");
        btnSubir.classList.add("active");
        wrapCanvas.style.display = "none";
        wrapArchivo.style.display = "block";
    }
};

async function guardarDonanteConFirma() {
    const msgEl = document.getElementById("consentMsg");
    if(!msgEl) return;

    msgEl.style.display = "block";
    msgEl.style.color = "#475569";
    msgEl.style.background = "#f1f5f9";
    msgEl.textContent = "Procesando firma y guardando datos...";

    try {
        let base64Firma = "";

        if (modoFirmaActual === 'dibujar') {
            const canvas = document.getElementById("canvasFirma");
            const ctx = canvas.getContext('2d');
            const pixelBuffer = new Uint32Array(ctx.getImageData(0, 0, canvas.width, canvas.height).data.buffer);
            const hasPixels = pixelBuffer.some(color => color !== 0);

            if (!hasPixels) {
                throw new Error("El lienzo está vacío. Por favor, traza tu firma.");
            }
            
            base64Firma = canvas.toDataURL("image/png");
        } else {
            const fileInput = document.getElementById("archivoFirma");
            if (!fileInput.files || fileInput.files.length === 0) {
                throw new Error("Por favor, selecciona un archivo (Imagen o PDF) para el consentimiento.");
            }
            base64Firma = await fileToBase64(fileInput.files[0]);
        }

        const donorPayload = {
            nombres: document.getElementById("nombres").value,
            apellidos: document.getElementById("apellidos").value,
            documento: document.getElementById("donanteId").value, 
            fechaNacimiento: document.getElementById("fechaNacimiento").value,
            tipoSangre: document.getElementById("tipoSangre").value,
            peso: parseFloat(document.getElementById("peso").value),
            telefono: document.getElementById("telefono").value,
            correo: document.getElementById("correo").value,
            direccion: document.getElementById("direccion").value,
            aceptaConsentimiento: true
        };

        const donanteRes = await fetchAPI("/donantes", {
            method: "POST",
            body: JSON.stringify(donorPayload)
        });

        const consentPayload = {
            donanteId: donanteRes.id, 
            aceptaConsentimiento: true,
            firmaConsentimiento: base64Firma,
            fechaFirma: new Date().toISOString().split('T')[0]
        };

        await fetchAPI("/consentimientos", {
            method: "POST",
            body: JSON.stringify(consentPayload)
        });

        msgEl.style.color = "#166534";
        msgEl.style.background = "#dcfce7";
        msgEl.innerHTML = "<i class='fa-solid fa-circle-check'></i> ¡Donante y consentimiento registrados exitosamente!";

        setTimeout(() => {
            closeModal('donorModal');
            document.getElementById("consentimientoForm").reset();
            msgEl.style.display = "none";
            
            const canvas = document.getElementById("canvasFirma");
            if (canvas) {
                const ctx = canvas.getContext("2d");
                ctx.clearRect(0, 0, canvas.width, canvas.height);
            }

            if(window.location.pathname.includes('donantes.html')) initDonantes(0);
        }, 2000);

    } catch (error) {
        msgEl.style.color = "#991b1b";
        msgEl.style.background = "#fee2e2";
        msgEl.innerHTML = `<i class="fa-solid fa-circle-exclamation"></i> Error: ${error.message}`;
    }
}


// ==========================================
// 6. MÓDULO DE DONACIONES (Nuevo Integrado)
// ==========================================

let donacionesCurrentPage = 0;
const donacionesPerPage = 8;
let donacionesCurrentData = [];
let selectedDonorId = null;
let searchTimeout;

// Inicialización de la vista Donaciones
async function initDonaciones() {
    const inputFecha = document.getElementById('fechaDonacion');
    if (inputFecha) inputFecha.valueAsDate = new Date();
    loadDonations();
}

// Cargar la tabla de Donaciones desde la API
window.loadDonations = async function() {
    const body = document.getElementById('donacionesBody');
    if(body) body.innerHTML = '<tr><td colspan="8" style="text-align:center;padding:3rem;color:#94a3b8;"><i class="fa-solid fa-spinner fa-spin"></i> Cargando donaciones...</td></tr>';
    
    try {
        const response = await fetchAPI(`/donaciones?page=${donacionesCurrentPage}&size=${donacionesPerPage}&sort=fechaDonacion,desc`);
        donacionesCurrentData = response.content || [];
        renderDonacionesTable(response);
        updateDonacionesStats(response);
    } catch (error) {
        if(body) body.innerHTML = `<tr><td colspan="8" style="text-align:center;color:#b91c1c;">Error: ${error.message}</td></tr>`;
    }
}

function statusClass(e) { 
    return (e === 'COMPLETADA' || e === 'PROCESADA' || e === 'REGISTRADA') ? 'ok' : 'blocked'; 
}

function initials(n) { 
    if (!n) return 'XX';
    return n.split(' ').slice(0, 2).map(w => w[0]).join('').toUpperCase(); 
}

function renderDonacionesTable(pageData) {
    const body = document.getElementById('donacionesBody');
    if (!donacionesCurrentData || donacionesCurrentData.length === 0) {
        body.innerHTML = '<tr><td colspan="8" style="text-align:center;padding:3rem;color:#94a3b8;">No hay donaciones registradas</td></tr>';
        document.getElementById('countLabel').textContent = '0 registros';
        document.getElementById('pageBtns').innerHTML = '';
        document.getElementById('pageInfo').textContent = 'Mostrando 0 registros';
        return;
    }

    body.innerHTML = donacionesCurrentData.map(d => {
        const fechaObj = new Date(d.fechaDonacion);
        const fechaFormat = isNaN(fechaObj.getTime()) ? '---' : fechaObj.toLocaleDateString();
        const tipoLimpio = d.tipoSangre ? d.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-') : 'N/A';
        
        return `
            <tr>
                <td><span class="code-pill">${d.codigoDonacion || `DON-${d.id}`}</span></td>
                <td>
                    <div class="donor-cell">
                        <div class="donor-avatar">${initials(d.nombreDonante)}</div>
                        <span style="font-weight:500">${d.nombreDonante}</span>
                    </div>
                </td>
                <td><span class="blood-chip">${tipoLimpio}</span></td>
                <td>
                    <div class="vol-bar"><div class="vol-fill" style="width:${(d.cantidadMl / 500) * 100}%"></div></div>
                    ${d.cantidadMl > 0 ? d.cantidadMl + ' mL' : '—'}
                </td>
                <td>${fechaFormat}</td>
                <td style="max-width:180px;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;color:#64748b;font-size:13px;" title="${d.observaciones || ''}">${d.observaciones || '—'}</td>
                <td><span class="status-pill ${statusClass(d.estado)}">${d.estado || 'PROCESADA'}</span></td>
                <td>
                    <div class="actions-cell">
                        <button class="action-btn" title="Exportar PDF" onclick="exportPDF(${d.donanteId})"><i class="fa-solid fa-file-pdf"></i></button>
                    </div>
                </td>
            </tr>
        `;
    }).join('');

    renderDonacionesPagination(pageData);
    document.getElementById('countLabel').textContent = `${pageData.totalElements} registros`;
}

function renderDonacionesPagination(pageData) {
    const btns = document.getElementById('pageBtns');
    btns.innerHTML = '';
    
    if (pageData.totalPages <= 0) return;

    const start = (pageData.number * pageData.size) + 1;
    const end = Math.min((pageData.number + 1) * pageData.size, pageData.totalElements);
    document.getElementById('pageInfo').textContent = `Mostrando ${start}–${end} de ${pageData.totalElements}`;

    for (let i = 0; i < pageData.totalPages; i++) {
        const b = document.createElement('button');
        b.className = 'page-btn' + (i === pageData.number ? ' active' : '');
        b.textContent = i + 1;
        b.onclick = () => { 
            donacionesCurrentPage = i; 
            loadDonations(); 
        };
        btns.appendChild(b);
    }
}

function updateDonacionesStats(pageData) {
    const statTotal = document.getElementById('statTotal');
    const statPage = document.getElementById('statPage');
    if(statTotal) statTotal.textContent = pageData.totalElements || 0;
    if(statPage) statPage.textContent = (pageData.number + 1) || 1;
}

// Búsqueda en el Modal
window.handleSearchInput = function() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(searchDonor, 800);
}

async function searchDonor() {
    const documento = document.getElementById('donorSearch').value.trim();
    const msg = document.getElementById('validationMsg');
    
    if (!documento) {
        msg.style.display = 'none';
        selectedDonorId = null;
        return;
    }

    msg.style.display = 'block';
    msg.style.background = '#f1f5f9';
    msg.style.color = '#475569';
    msg.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Consultando historial médico...';

    try {
        const donante = await fetchAPI(`/donantes/documento/${documento}`);
        selectedDonorId = donante.id;

        if (!donante.activo) {
            msg.style.background = '#fee2e2'; msg.style.color = '#b91c1c';
            msg.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> El donante se encuentra inactivo en el sistema.';
            selectedDonorId = null;
            return;
        }
        
        if (!donante.aceptaConsentimiento) {
            msg.style.background = '#fef9c3'; msg.style.color = '#a16207';
            msg.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> El donante no registra un consentimiento firmado.';
            selectedDonorId = null;
            return;
        }

        const tipoLimpio = donante.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-');
        msg.style.background = '#dcfce7'; msg.style.color = '#15803d';
        msg.innerHTML = `<i class="fa-solid fa-circle-check"></i> <strong>Donante Apto:</strong> ${donante.nombres} ${donante.apellidos} | RH: ${tipoLimpio}`;

    } catch (error) {
        selectedDonorId = null;
        msg.style.background = '#fee2e2'; msg.style.color = '#b91c1c';
        msg.innerHTML = `<i class="fa-solid fa-circle-xmark"></i> Documento no encontrado o error de conexión.`;
    }
}

// Registrar nueva donación
// --- REGISTRO DE DONACIÓN ---
async function saveDonation() {
    const msg = document.getElementById('validationMsg');

    // 1. Validaciones usando la UI en lugar de alerts
    if (!selectedDonorId) {
        msg.style.display = 'block';
        msg.style.background = '#fef9c3';
        msg.style.color = '#a16207';
        msg.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> Por favor, valide un documento de donante antes de registrar la extracción.';
        return;
    }

    const cantidad = document.getElementById('cantidadML').value;
    const obs = document.getElementById('observaciones').value;
    
    if(!cantidad || cantidad <= 0) {
        msg.style.display = 'block';
        msg.style.background = '#fef9c3';
        msg.style.color = '#a16207';
        msg.innerHTML = '<i class="fa-solid fa-triangle-exclamation"></i> La cantidad de mililitros debe ser mayor a 0';
        return;
    }

    try {
        // 2. Mensaje de carga
        msg.style.display = 'block';
        msg.style.background = '#f1f5f9';
        msg.style.color = '#475569';
        msg.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Registrando y validando reglas de negocio...';
        
        await fetchAPI('/donaciones', {
            method: 'POST',
            body: JSON.stringify({
                donanteId: selectedDonorId,
                cantidadMl: parseInt(cantidad),
                observaciones: obs
            })
        });
        
        // 3. Mensaje de éxito reemplazando el alert
        msg.style.background = '#dcfce7';
        msg.style.color = '#15803d';
        msg.innerHTML = '<i class="fa-solid fa-circle-check"></i> ¡Donación registrada con éxito en el banco de sangre!';
        
        // 4. Temporizador para cerrar el modal después de mostrar el éxito
        setTimeout(() => {
            closeModal('donationModal');
            selectedDonorId = null;
            document.getElementById('donorSearch').value = '';
            document.getElementById('cantidadML').value = '450';
            document.getElementById('observaciones').value = '';
            msg.style.display = 'none';
            
            // Recargar desde la primera página
            currentPage = 0;
            loadDonations();
        }, 1500); // 1500 milisegundos (1.5 segundos) de espera

    } catch (error) {
        // 5. Manejo de errores
        msg.style.background = '#fee2e2'; 
        msg.style.color = '#b91c1c';
        msg.innerHTML = `<i class="fa-solid fa-circle-xmark"></i> <strong>No Apto:</strong> ${error.message}`;
    }
}

window.exportPDF = function(donanteId) {
    const token = getToken();
    window.open(`${API_URL}/donaciones/donante/${donanteId}/historial-pdf`, "_blank");
}

function exportarPDFGeneral() {
    const token = getToken();

    window.open(`${API_URL}/donaciones/reporte-pdf`, "_blank");
}

window.exportCSV = function() {
    if(!donacionesCurrentData || !donacionesCurrentData.length) {
        alert('No hay datos en la vista actual para exportar.');
        return;
    }
    
    const rows = [['Código','Donante','Tipo RH','mL Extraídos','Fecha Donación','Estado','Observaciones']];
    donacionesCurrentData.forEach(d => {
        const fecha = new Date(d.fechaDonacion).toLocaleDateString();
        const tipo = d.tipoSangre ? d.tipoSangre.replace('_POSITIVO', '+').replace('_NEGATIVO', '-') : '';
        rows.push([d.codigoDonacion, d.nombreDonante, tipo, d.cantidadMl, fecha, d.estado, d.observaciones || '']);
    });
    
    const csv = rows.map(r => r.map(field => `"${field}"`).join(',')).join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement("a");
    
    if (link.download !== undefined) {
        const url = URL.createObjectURL(blob);
        link.setAttribute("href", url);
        link.setAttribute("download", "reporte_donaciones_vista.csv");
        link.style.visibility = 'hidden';
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
}