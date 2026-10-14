// Credenciales del usuario (se mandan en cada petición con Basic Auth)
let credenciales = null;

// ===================== Funciones generales =====================

// Hace una petición a la API con el usuario que inició sesión
async function api(url, metodo = "GET", datos = null) {
    const opciones = {
        method: metodo,
        headers: {
            "Authorization": "Basic " + credenciales,
            // Evita que el navegador muestre su ventana de login
            "X-Requested-With": "XMLHttpRequest"
        }
    };
    if (datos) {
        opciones.headers["Content-Type"] = "application/json";
        opciones.body = JSON.stringify(datos);
    }

    const respuesta = await fetch(url, opciones);

    if (respuesta.status === 401) {
        throw new Error("Usuario o contraseña incorrectos");
    }
    if (respuesta.status === 403) {
        throw new Error("No tienes permiso para esta acción");
    }
    if (!respuesta.ok) {
        const texto = await respuesta.text();
        throw new Error(texto || "Error " + respuesta.status);
    }
    if (respuesta.status === 204) {
        return null;
    }
    return respuesta.json();
}

// Muestra un mensaje arriba de la página
function mostrarMensaje(texto, tipo = "ok") {
    const mensaje = document.getElementById("mensaje");
    mensaje.textContent = texto;
    mensaje.className = "mensaje " + tipo;
    setTimeout(() => mensaje.classList.add("oculto"), 3500);
}

// Convierte los campos de un formulario en un objeto
function datosFormulario(form) {
    return Object.fromEntries(new FormData(form).entries());
}

function dinero(valor) {
    return "$" + Number(valor).toFixed(2);
}

// ===================== Login =====================

document.getElementById("formLogin").addEventListener("submit", async (e) => {
    e.preventDefault();
    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;
    credenciales = btoa(email + ":" + password);

    try {
        await api("/api/vehiculos"); // si responde bien, las credenciales son correctas
        document.getElementById("usuarioActual").textContent = email;
        document.getElementById("pantallaLogin").classList.add("oculto");
        document.getElementById("pantallaPanel").classList.remove("oculto");
        cargarVehiculos();
    } catch (error) {
        document.getElementById("loginError").textContent = error.message;
        credenciales = null;
    }
});

document.getElementById("btnSalir").addEventListener("click", () => {
    credenciales = null;
    document.getElementById("loginPassword").value = "";
    document.getElementById("loginError").textContent = "";
    document.getElementById("pantallaPanel").classList.add("oculto");
    document.getElementById("pantallaLogin").classList.remove("oculto");
});

// ===================== Pestañas =====================

document.querySelectorAll(".tab").forEach(boton => {
    boton.addEventListener("click", () => {
        document.querySelectorAll(".tab").forEach(b => b.classList.remove("activa"));
        document.querySelectorAll(".contenido").forEach(c => c.classList.add("oculto"));
        boton.classList.add("activa");
        const tab = boton.dataset.tab;
        document.getElementById("tab-" + tab).classList.remove("oculto");

        if (tab === "vehiculos") cargarVehiculos();
        if (tab === "clientes") cargarClientes();
        if (tab === "alquileres") cargarAlquileres();
        if (tab === "oficinas") cargarOficinas();
    });
});

// ===================== Vehículos =====================

async function cargarVehiculos() {
    const estado = document.getElementById("filtroEstado").value;
    const url = estado ? "/api/vehiculos/estado/" + estado : "/api/vehiculos";
    try {
        const vehiculos = await api(url);
        const tabla = document.getElementById("tablaVehiculos");
        if (vehiculos.length === 0) {
            tabla.innerHTML = '<tr><td colspan="8" class="vacio">No hay vehículos</td></tr>';
            return;
        }
        tabla.innerHTML = vehiculos.map(v => `
            <tr>
                <td>${v.idVehiculo}</td>
                <td>${v.marca}</td>
                <td>${v.modelo}</td>
                <td>${v.anio}</td>
                <td>${v.matricula}</td>
                <td>${dinero(v.tarifaDiaria)}</td>
                <td><span class="estado ${v.estado}">${v.estado}</span></td>
                <td class="acciones"><button class="chico peligro" onclick="eliminar('vehiculos', ${v.idVehiculo})">Eliminar</button></td>
            </tr>`).join("");
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}

document.getElementById("filtroEstado").addEventListener("change", cargarVehiculos);

document.getElementById("formVehiculo").addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
        await api("/api/vehiculos", "POST", datosFormulario(e.target));
        e.target.reset();
        mostrarMensaje("Vehículo guardado");
        cargarVehiculos();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
});

// ===================== Clientes =====================

async function cargarClientes() {
    try {
        const clientes = await api("/api/clientes");
        const tabla = document.getElementById("tablaClientes");
        if (clientes.length === 0) {
            tabla.innerHTML = '<tr><td colspan="6" class="vacio">No hay clientes</td></tr>';
            return;
        }
        tabla.innerHTML = clientes.map(c => `
            <tr>
                <td>${c.idCliente}</td>
                <td>${c.nombre} ${c.apellido}</td>
                <td>${c.email}</td>
                <td>${c.licenciaConducir}</td>
                <td>${c.telefono || ""}</td>
                <td class="acciones"><button class="chico peligro" onclick="eliminar('clientes', ${c.idCliente})">Eliminar</button></td>
            </tr>`).join("");
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}

document.getElementById("formCliente").addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
        await api("/api/clientes", "POST", datosFormulario(e.target));
        e.target.reset();
        mostrarMensaje("Cliente guardado");
        cargarClientes();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
});

// ===================== Alquileres =====================

async function cargarAlquileres() {
    try {
        const alquileres = await api("/api/alquileres");
        const tabla = document.getElementById("tablaAlquileres");
        if (alquileres.length === 0) {
            tabla.innerHTML = '<tr><td colspan="8" class="vacio">No hay alquileres</td></tr>';
        } else {
            tabla.innerHTML = alquileres.map(a => `
                <tr>
                    <td>${a.idAlquiler}</td>
                    <td>${a.cliente.nombre} ${a.cliente.apellido}</td>
                    <td>${a.vehiculo.marca} ${a.vehiculo.modelo}</td>
                    <td>${a.fechaAlquiler}</td>
                    <td>${a.fechaDevolucion}</td>
                    <td>${dinero(a.precioTotal)}</td>
                    <td><span class="estado ${a.vehiculo.estado}">${a.vehiculo.estado}</span></td>
                    <td class="acciones">
                        ${a.vehiculo.estado === "ALQUILADO"
                            ? `<button class="chico" onclick="devolver(${a.idAlquiler})">Devolver</button>`
                            : ""}
                        <button class="chico peligro" onclick="eliminar('alquileres', ${a.idAlquiler})">Eliminar</button>
                    </td>
                </tr>`).join("");
        }
        await cargarOpcionesAlquiler();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}

// Llena las listas de clientes y vehículos disponibles del formulario
async function cargarOpcionesAlquiler() {
    const clientes = await api("/api/clientes");
    const vehiculos = await api("/api/vehiculos/estado/DISPONIBLE");

    document.getElementById("selectCliente").innerHTML = clientes
        .map(c => `<option value="${c.idCliente}">${c.nombre} ${c.apellido}</option>`).join("");
    document.getElementById("selectVehiculo").innerHTML = vehiculos
        .map(v => `<option value="${v.idVehiculo}">${v.marca} ${v.modelo} - ${dinero(v.tarifaDiaria)}/día</option>`).join("");
}

document.getElementById("formAlquiler").addEventListener("submit", async (e) => {
    e.preventDefault();
    const datos = datosFormulario(e.target);
    const alquiler = {
        idCliente: Number(datos.idCliente),
        idVehiculo: Number(datos.idVehiculo),
        fechaAlquiler: datos.fechaAlquiler,
        fechaDevolucion: datos.fechaDevolucion
    };
    try {
        const nuevo = await api("/api/alquileres", "POST", alquiler);
        e.target.reset();
        mostrarMensaje("Alquiler registrado. Total: " + dinero(nuevo.precioTotal));
        cargarAlquileres();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
});

async function devolver(id) {
    try {
        await api("/api/alquileres/" + id + "/devolver", "PUT");
        mostrarMensaje("Vehículo devuelto");
        cargarAlquileres();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}

// ===================== Oficinas =====================

async function cargarOficinas() {
    try {
        const oficinas = await api("/api/oficinas");
        const tabla = document.getElementById("tablaOficinas");
        if (oficinas.length === 0) {
            tabla.innerHTML = '<tr><td colspan="6" class="vacio">No hay oficinas</td></tr>';
            return;
        }
        tabla.innerHTML = oficinas.map(o => `
            <tr>
                <td>${o.idOficina}</td>
                <td>${o.nombre}</td>
                <td>${o.direccion}</td>
                <td>${o.ciudad}</td>
                <td>${o.region}</td>
                <td class="acciones"><button class="chico peligro" onclick="eliminar('oficinas', ${o.idOficina})">Eliminar</button></td>
            </tr>`).join("");
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}

document.getElementById("formOficina").addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
        await api("/api/oficinas", "POST", datosFormulario(e.target));
        e.target.reset();
        mostrarMensaje("Oficina guardada");
        cargarOficinas();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
});

// ===================== Eliminar (sirve para todas las secciones) =====================

async function eliminar(recurso, id) {
    if (!confirm("¿Seguro que quieres eliminar el registro " + id + "?")) {
        return;
    }
    try {
        await api("/api/" + recurso + "/" + id, "DELETE");
        mostrarMensaje("Registro eliminado");
        if (recurso === "vehiculos") cargarVehiculos();
        if (recurso === "clientes") cargarClientes();
        if (recurso === "alquileres") cargarAlquileres();
        if (recurso === "oficinas") cargarOficinas();
    } catch (error) {
        mostrarMensaje(error.message, "mal");
    }
}
