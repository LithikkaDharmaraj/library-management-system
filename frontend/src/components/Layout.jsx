import { NavLink } from 'react-router-dom'

const links = [
  { to: '/books', label: 'Books' },
  { to: '/users', label: 'Users' },
  { to: '/issue', label: 'Issue' },
  { to: '/returns', label: 'Returns' },
  { to: '/fines', label: 'Fines' }
]

export default function Layout({ children }) {
  return (
    <div className="layout">
      <nav className="sidebar">
        <div className="brand">Library System</div>
        {links.map((l) => (
          <NavLink key={l.to} to={l.to} className={({ isActive }) => (isActive ? 'active' : '')}>
            {l.label}
          </NavLink>
        ))}
      </nav>
      <div className="content">{children}</div>
    </div>
  )
}
