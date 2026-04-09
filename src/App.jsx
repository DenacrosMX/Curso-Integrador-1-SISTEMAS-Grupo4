import { useState } from "react";

export default function PrototipoCondominios() {
  const [screen, setScreen] = useState("login");

  const stats = [
    { label: "Pagos del mes", value: "78%", sub: "156 de 200 cuotas pagadas" },
    { label: "Morosidad", value: "22%", sub: "44 cuotas pendientes" },
    { label: "Incidencias abiertas", value: "12", sub: "4 urgentes" },
    { label: "Usuarios activos", value: "86", sub: "últimos 7 días" },
  ];

  const incidencias = [
    { id: "#INC-101", titulo: "Fuga de agua en sótano", estado: "En proceso", prioridad: "Alta" },
    { id: "#INC-102", titulo: "Luz pasillo torre B", estado: "Pendiente", prioridad: "Media" },
    { id: "#INC-103", titulo: "Ascensor torre A", estado: "Resuelto", prioridad: "Alta" },
    { id: "#INC-104", titulo: "Puerta del garaje", estado: "Pendiente", prioridad: "Baja" },
  ];

  const pagos = [
    { depto: "Dpto. 302", residente: "Carlos Díaz", estado: "Pagado", monto: "S/ 250" },
    { depto: "Dpto. 504", residente: "Ana Torres", estado: "Pendiente", monto: "S/ 250" },
    { depto: "Dpto. 201", residente: "Luis Rojas", estado: "Vencido", monto: "S/ 250" },
    { depto: "Dpto. 408", residente: "María López", estado: "Pagado", monto: "S/ 250" },
  ];

  const comunicados = [
    { titulo: "Mantenimiento de bomba de agua", detalle: "Se realizará el viernes a las 6:00 p. m." },
    { titulo: "Reunión de junta directiva", detalle: "Sábado a las 10:00 a. m. en el salón comunal." },
    { titulo: "Recordatorio de pago", detalle: "La cuota de mantenimiento vence el día 30." },
  ];

  const residents = [
    { nombre: "Carlos Díaz", depto: "302", estado: "Activo" },
    { nombre: "Ana Torres", depto: "504", estado: "Moroso" },
    { nombre: "Luis Rojas", depto: "201", estado: "Activo" },
    { nombre: "María López", depto: "408", estado: "Activo" },
  ];

  const badge = (text, type) => {
    const styles = {
      ok: "bg-green-100 text-green-700",
      warn: "bg-yellow-100 text-yellow-700",
      danger: "bg-red-100 text-red-700",
      info: "bg-blue-100 text-blue-700",
      gray: "bg-slate-100 text-slate-700",
    };
    return <span className={`px-2 py-1 rounded-full text-xs font-medium ${styles[type]}`}>{text}</span>;
  };

  const menuItems = [
    { key: "dashboard", label: "Dashboard" },
    { key: "pagos", label: "Pagos" },
    { key: "incidencias", label: "Incidencias" },
    { key: "comunicados", label: "Comunicados" },
    { key: "residentes", label: "Residentes" },
  ];

  const Sidebar = () => (
    <aside className="col-span-12 md:col-span-3 lg:col-span-2 bg-slate-900 text-white p-6">
      <div className="mb-8">
        <h1 className="text-xl font-bold">CondoGestion</h1>
        <p className="text-sm text-slate-300">Panel administrativo</p>
      </div>

      <nav className="space-y-2 text-sm">
        {menuItems.map((item) => (
          <button
            key={item.key}
            onClick={() => setScreen(item.key)}
            className={`w-full text-left rounded-2xl px-4 py-3 ${screen === item.key ? "bg-white/15 font-medium" : "hover:bg-white/10"}`}
          >
            {item.label}
          </button>
        ))}
      </nav>

      <button
        onClick={() => setScreen("login")}
        className="mt-8 w-full rounded-2xl bg-white text-slate-900 px-4 py-3 text-sm font-medium"
      >
        Cerrar sesión
      </button>
    </aside>
  );

  const Layout = ({ title, subtitle, children }) => (
    <div className="min-h-screen bg-slate-100 text-slate-800">
      <div className="grid grid-cols-12 min-h-screen">
        <Sidebar />
        <main className="col-span-12 md:col-span-9 lg:col-span-10 p-4 md:p-8">
          <header className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-8">
            <div>
              <h2 className="text-3xl font-bold">{title}</h2>
              <p className="text-slate-600 mt-1">{subtitle}</p>
            </div>
            <div className="flex gap-3 flex-wrap">
              <button className="bg-white rounded-2xl px-4 py-2 shadow-sm">Exportar</button>
              <button className="bg-slate-900 text-white rounded-2xl px-4 py-2 shadow-sm">Nueva acción</button>
            </div>
          </header>
          {children}
        </main>
      </div>
    </div>
  );

  if (screen === "login") {
    return (
      <div className="min-h-screen bg-slate-100 flex items-center justify-center p-6">
        <div className="w-full max-w-5xl grid md:grid-cols-2 bg-white rounded-3xl shadow-xl overflow-hidden">
          <div className="bg-slate-900 text-white p-10 flex flex-col justify-center">
            <h1 className="text-4xl font-bold mb-4">CondoGestion</h1>
            <p className="text-slate-300 text-base leading-7">
              Plataforma web para la gestión digital de condominios y edificios residenciales.
            </p>
            <div className="mt-8 space-y-3 text-sm text-slate-300">
              <p>• Control de pagos y morosidad</p>
              <p>• Registro y seguimiento de incidencias</p>
              <p>• Comunicados centralizados</p>
              <p>• Dashboard administrativo</p>
            </div>
          </div>

          <div className="p-10 flex flex-col justify-center">
            <h2 className="text-2xl font-bold mb-2">Iniciar sesión</h2>
            <p className="text-slate-500 mb-8">Ingrese sus credenciales para acceder al sistema</p>

            <div className="space-y-4">
              <div>
                <label className="block text-sm font-medium mb-2">Correo electrónico</label>
                <input className="w-full border rounded-2xl px-4 py-3" placeholder="admin@condominio.com" />
              </div>
              <div>
                <label className="block text-sm font-medium mb-2">Contraseña</label>
                <input type="password" className="w-full border rounded-2xl px-4 py-3" placeholder="********" />
              </div>
              <button
                onClick={() => setScreen("dashboard")}
                className="w-full bg-slate-900 text-white rounded-2xl py-3 font-medium"
              >
                Ingresar
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (screen === "dashboard") {
    return (
      <Layout
        title="Dashboard General"
        subtitle="Resumen de pagos, incidencias, comunicados y actividad del condominio"
      >
        <section className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-8">
          {stats.map((item) => (
            <div key={item.label} className="bg-white rounded-2xl shadow-sm p-5">
              <p className="text-sm text-slate-500">{item.label}</p>
              <p className="text-3xl font-bold mt-2">{item.value}</p>
              <p className="text-sm text-slate-500 mt-1">{item.sub}</p>
            </div>
          ))}
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <div className="xl:col-span-2 bg-white rounded-2xl shadow-sm p-5">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold">Estado de pagos</h3>
              {badge("Actualizado hoy", "info")}
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="text-left text-slate-500 border-b">
                    <th className="pb-3">Departamento</th>
                    <th className="pb-3">Residente</th>
                    <th className="pb-3">Estado</th>
                    <th className="pb-3">Monto</th>
                  </tr>
                </thead>
                <tbody>
                  {pagos.slice(0, 3).map((p) => (
                    <tr key={p.depto} className="border-b last:border-0">
                      <td className="py-3">{p.depto}</td>
                      <td className="py-3">{p.residente}</td>
                      <td className="py-3">
                        {p.estado === "Pagado" && badge(p.estado, "ok")}
                        {p.estado === "Pendiente" && badge(p.estado, "warn")}
                        {p.estado === "Vencido" && badge(p.estado, "danger")}
                      </td>
                      <td className="py-3 font-medium">{p.monto}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>

          <div className="bg-white rounded-2xl shadow-sm p-5">
            <h3 className="text-lg font-semibold mb-4">Comunicados recientes</h3>
            <div className="space-y-3">
              {comunicados.map((c, idx) => (
                <div key={idx} className="bg-slate-50 rounded-2xl p-4 text-sm">
                  <p className="font-medium">{c.titulo}</p>
                  <p className="text-slate-500 mt-1">{c.detalle}</p>
                </div>
              ))}
            </div>
          </div>
        </section>
      </Layout>
    );
  }

  if (screen === "pagos") {
    return (
      <Layout title="Módulo de Pagos" subtitle="Control de cuotas, pagos realizados y morosidad de residentes">
        <div className="bg-white rounded-2xl shadow-sm p-5 mb-6">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <input className="border rounded-2xl px-4 py-3" placeholder="Buscar residente" />
            <select className="border rounded-2xl px-4 py-3">
              <option>Todos los estados</option>
              <option>Pagado</option>
              <option>Pendiente</option>
              <option>Vencido</option>
            </select>
            <input className="border rounded-2xl px-4 py-3" placeholder="Mes" />
            <button className="bg-slate-900 text-white rounded-2xl px-4 py-3">Registrar pago</button>
          </div>
        </div>

        <div className="bg-white rounded-2xl shadow-sm p-5">
          <div className="overflow-x-auto">
            <table className="w-full text-sm">
              <thead>
                <tr className="text-left text-slate-500 border-b">
                  <th className="pb-3">Departamento</th>
                  <th className="pb-3">Residente</th>
                  <th className="pb-3">Estado</th>
                  <th className="pb-3">Monto</th>
                  <th className="pb-3">Acción</th>
                </tr>
              </thead>
              <tbody>
                {pagos.map((p) => (
                  <tr key={p.depto} className="border-b last:border-0">
                    <td className="py-3">{p.depto}</td>
                    <td className="py-3">{p.residente}</td>
                    <td className="py-3">
                      {p.estado === "Pagado" && badge(p.estado, "ok")}
                      {p.estado === "Pendiente" && badge(p.estado, "warn")}
                      {p.estado === "Vencido" && badge(p.estado, "danger")}
                    </td>
                    <td className="py-3 font-medium">{p.monto}</td>
                    <td className="py-3"><button className="text-sm bg-slate-100 px-3 py-2 rounded-xl">Ver detalle</button></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </Layout>
    );
  }

  if (screen === "incidencias") {
    return (
      <Layout title="Módulo de Incidencias" subtitle="Registro, seguimiento y estado de reclamos o problemas del condominio">
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <div className="xl:col-span-2 bg-white rounded-2xl shadow-sm p-5">
            <h3 className="text-lg font-semibold mb-4">Incidencias registradas</h3>
            <div className="space-y-3">
              {incidencias.map((i) => (
                <div key={i.id} className="border rounded-2xl p-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3">
                  <div>
                    <p className="font-semibold">{i.id} - {i.titulo}</p>
                    <p className="text-sm text-slate-500">Prioridad: {i.prioridad}</p>
                  </div>
                  <div>
                    {i.estado === "Resuelto" ? badge(i.estado, "ok") : i.estado === "En proceso" ? badge(i.estado, "info") : badge(i.estado, "warn")}
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white rounded-2xl shadow-sm p-5">
            <h3 className="text-lg font-semibold mb-4">Registrar incidencia</h3>
            <div className="space-y-4">
              <input className="w-full border rounded-2xl px-4 py-3" placeholder="Título" />
              <select className="w-full border rounded-2xl px-4 py-3">
                <option>Seleccionar prioridad</option>
                <option>Alta</option>
                <option>Media</option>
                <option>Baja</option>
              </select>
              <textarea className="w-full border rounded-2xl px-4 py-3 h-32" placeholder="Descripción"></textarea>
              <button className="w-full bg-slate-900 text-white rounded-2xl py-3">Guardar incidencia</button>
            </div>
          </div>
        </div>
      </Layout>
    );
  }

  if (screen === "comunicados") {
    return (
      <Layout title="Módulo de Comunicados" subtitle="Gestión de avisos y mensajes dirigidos a los residentes">
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <div className="xl:col-span-2 bg-white rounded-2xl shadow-sm p-5">
            <h3 className="text-lg font-semibold mb-4">Comunicados emitidos</h3>
            <div className="space-y-4">
              {comunicados.map((c, idx) => (
                <div key={idx} className="border rounded-2xl p-4">
                  <p className="font-semibold">{c.titulo}</p>
                  <p className="text-slate-500 mt-1">{c.detalle}</p>
                  <div className="mt-3">{badge("Publicado", "ok")}</div>
                </div>
              ))}
            </div>
          </div>

          <div className="bg-white rounded-2xl shadow-sm p-5">
            <h3 className="text-lg font-semibold mb-4">Nuevo comunicado</h3>
            <div className="space-y-4">
              <input className="w-full border rounded-2xl px-4 py-3" placeholder="Asunto" />
              <textarea className="w-full border rounded-2xl px-4 py-3 h-36" placeholder="Contenido del comunicado"></textarea>
              <button className="w-full bg-slate-900 text-white rounded-2xl py-3">Publicar comunicado</button>
            </div>
          </div>
        </div>
      </Layout>
    );
  }

  return (
    <Layout title="Módulo de Residentes" subtitle="Consulta general de usuarios, departamentos y estado dentro del sistema">
      <div className="bg-white rounded-2xl shadow-sm p-5">
        <div className="overflow-x-auto">
          <table className="w-full text-sm">
            <thead>
              <tr className="text-left text-slate-500 border-b">
                <th className="pb-3">Residente</th>
                <th className="pb-3">Departamento</th>
                <th className="pb-3">Estado</th>
                <th className="pb-3">Acción</th>
              </tr>
            </thead>
            <tbody>
              {residents.map((r) => (
                <tr key={r.nombre} className="border-b last:border-0">
                  <td className="py-3">{r.nombre}</td>
                  <td className="py-3">{r.depto}</td>
                  <td className="py-3">{r.estado === "Activo" ? badge(r.estado, "ok") : badge(r.estado, "warn")}</td>
                  <td className="py-3"><button className="text-sm bg-slate-100 px-3 py-2 rounded-xl">Ver perfil</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </Layout>
  );
}
