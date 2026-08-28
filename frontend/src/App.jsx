import { Navigate, Route, Routes } from 'react-router-dom'
import Layout from './components/Layout.jsx'
import Books from './pages/Books.jsx'
import Users from './pages/Users.jsx'
import Issue from './pages/Issue.jsx'
import Returns from './pages/Returns.jsx'
import Fines from './pages/Fines.jsx'

export default function App() {
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<Navigate to="/books" replace />} />
        <Route path="/books" element={<Books />} />
        <Route path="/users" element={<Users />} />
        <Route path="/issue" element={<Issue />} />
        <Route path="/returns" element={<Returns />} />
        <Route path="/fines" element={<Fines />} />
      </Routes>
    </Layout>
  )
}
