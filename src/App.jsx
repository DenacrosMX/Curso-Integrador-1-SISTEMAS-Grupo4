import { useState } from "react";
import { motion } from "framer-motion";
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  CartesianGrid,
  XAxis,
  YAxis,
  Tooltip,
  BarChart,
  Bar,
} from "recharts";

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

  const pagosTrend = [
    { mes: "Ene", pagos: 52 },
    { mes: "Feb", pagos: 64 },
    { mes: "Mar", pagos: 60 },
    { mes: "Abr", pagos: 72 },
    { mes: "May", pagos: 78 },
    { mes: "Jun", pagos: 84 },
  ];

  const incidenciasData = [
    { nombre: "Resueltas", total: 18 },
    { nombre: "Proceso", total: 8 },
    { nombre: "Pendientes", total: 5 },
  ];

  const badge = (text, type) => {
    const styles = {
      ok: "bg-emerald-50 text-emerald-700 border border-emerald-100",
      warn: "bg-amber-50 text-amber-700 border border-amber-100",
      danger: "bg-red-50 text-red-700 border border-red-100",
      info: "bg-blue-50 text-blue-700 border border-blue-100",
      gray: "bg-slate-100 text-slate-700 border border-slate-200",
    };
    return <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${styles[type]}`}>{text}</span>;
  };

  const menuItems = [
    { key: "dashboard", label: "Dashboard" },
    { key: "pagos", label: "Pagos" },
    { key: "incidencias", label: "Incidencias" },
    { key: "comunicados", label: "Comunicados" },
    { key: "residentes", label: "Residentes" },
  ];

  const GlassCard = ({ children, className = "" }) => (
    <motion.div
      initial={{ opacity: 0, y: 14 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.3 }}
      className={`bg-white rounded-3xl shadow-sm border border-slate-200 ${className}`}
    >
      {children}
    </motion.div>
  );

  const Sidebar = () => (
    <aside className="col-span-12 lg:col-span-2 bg-slate-950 text-white p-5 lg:min-h-screen">
      <div className="mb-8">
        <div className="inline-flex items-center gap-2 rounded-full bg-white/5 border border-white/10 px-3 py-1.5 text-xs text-slate-300 mb-4">
          <span className="h-2 w-2 rounded-full bg-emerald-400" />
          Sistema en línea
        </div>
        <h1 className="text-2xl font-bold tracking-tight">CondoGestion</h1>
        <p className="text-sm text-slate-400 mt-1">Panel administrativo</p>
      </div>

      <nav className="space-y-2 text-sm">
        {menuItems.map((item) => (
          <button
            key={item.key}
            onClick={() => setScreen(item.key)}
            className={`w-full text-left rounded-2xl px-4 py-3 transition ${screen === item.key ? "bg-gradient-to-r from-blue-700 to-slate-800 font-semibold" : "text-slate-300 hover:bg-white/5 hover:text-white"}`}
          >
            {item.label}
          </button>
        ))}
      </nav>

      <div className="mt-8 rounded-3xl border border-white/10 bg-white/5 p-4">
        <p className="text-xs uppercase tracking-wide text-slate-400 mb-1">Resumen rápido</p>
        <p className="text-2xl font-bold">86</p>
        <p className="text-sm text-slate-300">usuarios activos esta semana</p>
      </div>

      <button
        onClick={() => setScreen("login")}
        className="mt-8 w-full rounded-2xl bg-white text-slate-900 px-4 py-3 text-sm font-semibold hover:bg-slate-100 transition"
      >
        Cerrar sesión
      </button>
    </aside>
  );

  const Layout = ({ title, subtitle, children }) => (
    <div className="min-h-screen bg-slate-100 text-slate-800 overflow-x-hidden">
      <div className="grid grid-cols-12 min-h-screen max-w-none">
        <Sidebar />
        <main className="col-span-12 lg:col-span-10 p-4 md:p-6 xl:p-8 bg-[radial-gradient(circle_at_top_right,rgba(59,130,246,0.08),transparent_25%)] overflow-x-hidden">
          <motion.header
            initial={{ opacity: 0, y: -10 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.25 }}
            className="flex flex-col xl:flex-row xl:items-center xl:justify-between gap-5 mb-8"
          >
            <div>
              <div className="inline-flex items-center gap-2 rounded-full bg-blue-50 px-3 py-1 text-xs font-semibold text-blue-700 mb-3">
                <span className="h-2 w-2 rounded-full bg-blue-600" />
                Vista de administración
              </div>
              <h2 className="text-3xl md:text-4xl font-bold tracking-tight">{title}</h2>
              <p className="text-slate-600 mt-2 max-w-2xl leading-7">{subtitle}</p>
            </div>
            <div className="flex gap-3 flex-wrap">
              <button className="bg-white rounded-2xl px-4 py-3 shadow-sm border border-slate-200 font-medium hover:shadow-md transition">Exportar</button>
              <button className="bg-gradient-to-r from-slate-900 to-blue-900 text-white rounded-2xl px-5 py-3 shadow-lg shadow-blue-900/20 font-semibold hover:-translate-y-0.5 transition">Nueva acción</button>
            </div>
          </motion.header>
          {children}
        </main>
      </div>
    </div>
  );

  if (screen === "login") {
    return (
      <div className="min-h-screen bg-slate-950 relative overflow-hidden">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,_rgba(59,130,246,0.18),_transparent_28%),radial-gradient(circle_at_bottom_right,_rgba(14,165,233,0.14),_transparent_24%)]" />
        <div className="relative min-h-screen flex items-center justify-center px-4 py-6 md:px-8">
          <motion.div
            initial={{ opacity: 0, y: 20 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.35 }}
            className="w-full max-w-6xl grid lg:grid-cols-2 rounded-[28px] overflow-hidden border border-white/10 bg-white/5 backdrop-blur-xl shadow-[0_20px_70px_rgba(15,23,42,0.35)]"
          >
            <div className="relative bg-gradient-to-br from-slate-950 via-slate-900 to-blue-950 text-white p-8 md:p-10 lg:p-12 flex flex-col justify-center">
              <div className="inline-flex items-center gap-2 rounded-full border border-white/10 bg-white/5 px-4 py-2 text-xs font-medium tracking-wide text-slate-200 mb-6 w-fit">
                <span className="h-2 w-2 rounded-full bg-emerald-400" />
                Plataforma residencial inteligente
              </div>

              <h1 className="text-4xl md:text-5xl font-bold tracking-tight mb-4">CondoGestion</h1>
              <p className="text-slate-300 text-base md:text-lg leading-8 max-w-xl mb-8">
                Administra pagos, incidencias, comunicados y residentes desde una experiencia moderna, clara y ordenada.
              </p>

              <div className="grid sm:grid-cols-2 gap-4 mb-8">
                <div className="rounded-3xl border border-white/10 bg-white/5 p-5">
                  <p className="text-3xl font-bold">78%</p>
                  <p className="text-sm text-slate-300 mt-1">Pagos al día este mes</p>
                </div>
                <div className="rounded-3xl border border-white/10 bg-white/5 p-5">
                  <p className="text-3xl font-bold">12</p>
                  <p className="text-sm text-slate-300 mt-1">Incidencias activas</p>
                </div>
              </div>

              <div className="space-y-3">
                {[
                  "Control de pagos y morosidad en tiempo real",
                  "Seguimiento de incidencias con prioridad y estado",
                  "Comunicados centralizados para residentes",
                ].map((item) => (
                  <div key={item} className="flex items-start gap-3 rounded-2xl border border-white/10 bg-white/5 px-4 py-3">
                    <div className="mt-1 h-2.5 w-2.5 rounded-full bg-cyan-400 shrink-0" />
                    <p className="text-sm text-slate-200">{item}</p>
                  </div>
                ))}
              </div>
            </div>

            <div className="bg-white p-6 md:p-8 lg:p-10 flex items-center">
              <div className="w-full max-w-md mx-auto">
                <p className="text-sm font-medium text-blue-700 mb-2">Bienvenido de nuevo</p>
                <h2 className="text-3xl font-bold text-slate-900 tracking-tight">Iniciar sesión</h2>
                <p className="text-slate-500 mt-2 leading-7 mb-8">
                  Accede al panel administrativo del condominio.
                </p>

                <div className="space-y-5">
                  <div>
                    <label className="block text-sm font-semibold text-slate-700 mb-2">Correo electrónico</label>
                    <div className="flex items-center rounded-2xl border border-slate-200 bg-white px-4 py-3 shadow-sm transition focus-within:border-blue-500 focus-within:ring-4 focus-within:ring-blue-100">
                      <span className="mr-3 text-slate-400">@</span>
                      <input className="w-full bg-transparent outline-none placeholder:text-slate-400 text-slate-800 min-w-0" placeholder="admin@condominio.com" />
                    </div>
                  </div>

                  <div>
                    <div className="flex items-center justify-between mb-2 gap-3">
                      <label className="block text-sm font-semibold text-slate-700">Contraseña</label>
                      <button className="text-xs font-medium text-blue-700 hover:text-blue-800 whitespace-nowrap">¿Olvidaste tu contraseña?</button>
                    </div>
                    <div className="flex items-center rounded-2xl border border-slate-200 bg-white px-4 py-3 shadow-sm transition focus-within:border-blue-500 focus-within:ring-4 focus-within:ring-blue-100">
                      <span className="mr-3 text-slate-400">*</span>
                      <input type="password" className="w-full bg-transparent outline-none placeholder:text-slate-400 text-slate-800 min-w-0" placeholder="********" />
                      <button className="ml-3 rounded-lg bg-slate-100 px-2 py-1 text-xs font-medium text-slate-600 hover:bg-slate-200 whitespace-nowrap">Ver</button>
                    </div>
                  </div>

                  <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3 text-sm">
                    <label className="flex items-center gap-2 text-slate-600">
                      <input type="checkbox" className="rounded border-slate-300" />
                      Recordarme
                    </label>
                    <span className="inline-flex items-center gap-2 rounded-full bg-emerald-50 px-3 py-1 text-emerald-700 font-medium w-fit">
                      <span className="h-2 w-2 rounded-full bg-emerald-500" />
                      Sistema activo
                    </span>
                  </div>

                  <motion.button
                    whileHover={{ y: -2 }}
                    whileTap={{ scale: 0.99 }}
                    onClick={() => setScreen("dashboard")}
                    className="w-full rounded-2xl bg-gradient-to-r from-slate-900 via-blue-900 to-slate-900 px-5 py-4 text-white font-semibold shadow-lg shadow-blue-900/20"
                  >
                    Ingresar al panel
                  </motion.button>
                </div>

                <div className="mt-8 rounded-3xl border border-slate-200 bg-slate-50 p-4">
                  <p className="text-sm font-semibold text-slate-800 mb-1">Acceso de demostración</p>
                  <p className="text-sm text-slate-500">Este login es un prototipo funcional orientado a exposición y validación visual.</p>
                </div>
              </div>
            </div>
          </motion.div>
        </div>
      </div>
    );
  }

  if (screen === "dashboard") {
    return (
      <Layout title="Dashboard General" subtitle="Resumen de pagos, incidencias, comunicados y actividad del condominio">
        <section className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-4 gap-4 mb-8">
          {stats.map((item, index) => (
            <motion.div
              key={item.label}
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: index * 0.05 }}
              className="bg-white rounded-3xl shadow-sm border border-slate-200 p-5 hover:shadow-md transition"
            >
              <div className="flex items-start justify-between">
                <div>
                  <p className="text-sm text-slate-500">{item.label}</p>
                  <p className="text-3xl font-bold mt-2 tracking-tight">{item.value}</p>
                </div>
                <div className="h-11 w-11 rounded-2xl bg-blue-50 flex items-center justify-center text-blue-700 font-bold shrink-0">•</div>
              </div>
              <p className="text-sm text-slate-500 mt-3">{item.sub}</p>
            </motion.div>
          ))}
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-3 gap-6 mb-6">
          <GlassCard className="xl:col-span-2 p-6 overflow-hidden">
            <div className="flex items-center justify-between mb-4 gap-3 flex-wrap">
              <h3 className="text-lg font-semibold">Tendencia de pagos</h3>
              {badge("Últimos 6 meses", "info")}
            </div>
            <div className="h-72 w-full">
              <ResponsiveContainer width="100%" height="100%">
                <AreaChart data={pagosTrend} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                  <defs>
                    <linearGradient id="colorPagos" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#2563eb" stopOpacity={0.35} />
                      <stop offset="95%" stopColor="#2563eb" stopOpacity={0.03} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                  <XAxis dataKey="mes" stroke="#64748b" />
                  <YAxis stroke="#64748b" />
                  <Tooltip />
                  <Area type="monotone" dataKey="pagos" stroke="#2563eb" strokeWidth={3} fill="url(#colorPagos)" />
                </AreaChart>
              </ResponsiveContainer>
            </div>
          </GlassCard>

          <GlassCard className="p-6 overflow-hidden">
            <div className="flex items-center justify-between mb-4 gap-3 flex-wrap">
              <h3 className="text-lg font-semibold">Estado de incidencias</h3>
              {badge("Tiempo real", "gray")}
            </div>
            <div className="h-72 w-full">
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={incidenciasData} margin={{ top: 10, right: 10, left: -20, bottom: 0 }}>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e2e8f0" />
                  <XAxis dataKey="nombre" stroke="#64748b" />
                  <YAxis stroke="#64748b" />
                  <Tooltip />
                  <Bar dataKey="total" radius={[12, 12, 0, 0]} fill="#1d4ed8" />
                </BarChart>
              </ResponsiveContainer>
            </div>
          </GlassCard>
        </section>

        <section className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <GlassCard className="xl:col-span-2 p-6 overflow-hidden">
            <div className="flex items-center justify-between mb-4 gap-3 flex-wrap">
              <h3 className="text-lg font-semibold">Estado de pagos</h3>
              {badge("Actualizado hoy", "info")}
            </div>
            <div className="overflow-x-auto">
              <table className="w-full text-sm min-w-[560px] xl:min-w-0">
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
                    <tr key={p.depto} className="border-b last:border-0 hover:bg-slate-50 transition">
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
          </GlassCard>

          <GlassCard className="p-6">
            <h3 className="text-lg font-semibold mb-4">Comunicados recientes</h3>
            <div className="space-y-3">
              {comunicados.map((c, idx) => (
                <motion.div key={idx} whileHover={{ y: -2 }} className="bg-slate-50 rounded-2xl p-4 text-sm border border-slate-200">
                  <p className="font-medium text-slate-800">{c.titulo}</p>
                  <p className="text-slate-500 mt-1">{c.detalle}</p>
                </motion.div>
              ))}
            </div>
          </GlassCard>
        </section>
      </Layout>
    );
  }

  if (screen === "pagos") {
    return (
      <Layout title="Módulo de Pagos" subtitle="Control de cuotas, pagos realizados y morosidad de residentes">
        <GlassCard className="p-5 mb-6">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <input className="border border-slate-200 rounded-2xl px-4 py-3 bg-white" placeholder="Buscar residente" />
            <select className="border border-slate-200 rounded-2xl px-4 py-3 bg-white">
              <option>Todos los estados</option>
              <option>Pagado</option>
              <option>Pendiente</option>
              <option>Vencido</option>
            </select>
            <input className="border border-slate-200 rounded-2xl px-4 py-3 bg-white" placeholder="Mes" />
            <button className="bg-gradient-to-r from-slate-900 to-blue-900 text-white rounded-2xl px-4 py-3 font-semibold">Registrar pago</button>
          </div>
        </GlassCard>

        <GlassCard className="p-5 overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-sm min-w-[720px] xl:min-w-0">
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
                  <tr key={p.depto} className="border-b last:border-0 hover:bg-slate-50 transition">
                    <td className="py-3">{p.depto}</td>
                    <td className="py-3">{p.residente}</td>
                    <td className="py-3">
                      {p.estado === "Pagado" && badge(p.estado, "ok")}
                      {p.estado === "Pendiente" && badge(p.estado, "warn")}
                      {p.estado === "Vencido" && badge(p.estado, "danger")}
                    </td>
                    <td className="py-3 font-medium">{p.monto}</td>
                    <td className="py-3"><button className="text-sm bg-slate-100 px-3 py-2 rounded-xl font-medium">Ver detalle</button></td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </GlassCard>
      </Layout>
    );
  }

  if (screen === "incidencias") {
    return (
      <Layout title="Módulo de Incidencias" subtitle="Registro, seguimiento y estado de reclamos o problemas del condominio">
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <GlassCard className="xl:col-span-2 p-5">
            <h3 className="text-lg font-semibold mb-4">Incidencias registradas</h3>
            <div className="space-y-3">
              {incidencias.map((i) => (
                <motion.div key={i.id} whileHover={{ y: -2 }} className="border border-slate-200 rounded-2xl p-4 flex flex-col md:flex-row md:items-center md:justify-between gap-3 bg-slate-50/60">
                  <div>
                    <p className="font-semibold">{i.id} - {i.titulo}</p>
                    <p className="text-sm text-slate-500">Prioridad: {i.prioridad}</p>
                  </div>
                  <div>
                    {i.estado === "Resuelto" ? badge(i.estado, "ok") : i.estado === "En proceso" ? badge(i.estado, "info") : badge(i.estado, "warn")}
                  </div>
                </motion.div>
              ))}
            </div>
          </GlassCard>

          <GlassCard className="p-5">
            <h3 className="text-lg font-semibold mb-4">Registrar incidencia</h3>
            <div className="space-y-4">
              <input className="w-full border border-slate-200 rounded-2xl px-4 py-3" placeholder="Título" />
              <select className="w-full border border-slate-200 rounded-2xl px-4 py-3">
                <option>Seleccionar prioridad</option>
                <option>Alta</option>
                <option>Media</option>
                <option>Baja</option>
              </select>
              <textarea className="w-full border border-slate-200 rounded-2xl px-4 py-3 h-32" placeholder="Descripción"></textarea>
              <button className="w-full bg-gradient-to-r from-slate-900 to-blue-900 text-white rounded-2xl py-3 font-semibold">Guardar incidencia</button>
            </div>
          </GlassCard>
        </div>
      </Layout>
    );
  }

  if (screen === "comunicados") {
    return (
      <Layout title="Módulo de Comunicados" subtitle="Gestión de avisos y mensajes dirigidos a los residentes">
        <div className="grid grid-cols-1 xl:grid-cols-3 gap-6">
          <GlassCard className="xl:col-span-2 p-5">
            <h3 className="text-lg font-semibold mb-4">Comunicados emitidos</h3>
            <div className="space-y-4">
              {comunicados.map((c, idx) => (
                <motion.div key={idx} whileHover={{ y: -2 }} className="border border-slate-200 rounded-2xl p-4 bg-slate-50/60">
                  <p className="font-semibold">{c.titulo}</p>
                  <p className="text-slate-500 mt-1">{c.detalle}</p>
                  <div className="mt-3">{badge("Publicado", "ok")}</div>
                </motion.div>
              ))}
            </div>
          </GlassCard>

          <GlassCard className="p-5">
            <h3 className="text-lg font-semibold mb-4">Nuevo comunicado</h3>
            <div className="space-y-4">
              <input className="w-full border border-slate-200 rounded-2xl px-4 py-3" placeholder="Asunto" />
              <textarea className="w-full border border-slate-200 rounded-2xl px-4 py-3 h-36" placeholder="Contenido del comunicado"></textarea>
              <button className="w-full bg-gradient-to-r from-slate-900 to-blue-900 text-white rounded-2xl py-3 font-semibold">Publicar comunicado</button>
            </div>
          </GlassCard>
        </div>
      </Layout>
    );
  }

  return (
    <Layout title="Módulo de Residentes" subtitle="Consulta general de usuarios, departamentos y estado dentro del sistema">
      <GlassCard className="p-5 overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full text-sm min-w-[640px] xl:min-w-0">
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
                <tr key={r.nombre} className="border-b last:border-0 hover:bg-slate-50 transition">
                  <td className="py-3">{r.nombre}</td>
                  <td className="py-3">{r.depto}</td>
                  <td className="py-3">{r.estado === "Activo" ? badge(r.estado, "ok") : badge(r.estado, "warn")}</td>
                  <td className="py-3"><button className="text-sm bg-slate-100 px-3 py-2 rounded-xl font-medium">Ver perfil</button></td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </GlassCard>
    </Layout>
  );
}