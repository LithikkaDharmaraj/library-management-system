import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Table from '../components/Table.jsx'
import Alert from '../components/Alert.jsx'

export default function Returns() {
  const [issues, setIssues] = useState([])
  const [books, setBooks] = useState([])
  const [users, setUsers] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [busy, setBusy] = useState(null)

  const load = useCallback(async () => {
    try {
      const [i, b, u] = await Promise.all([api.getIssues(), api.getBooks(), api.getUsers()])
      setIssues(i)
      setBooks(b)
      setUsers(u)
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const active = issues.filter((x) => x.status === 'ISSUED')

  const bookName = (id) => {
    const b = books.find((x) => x.id === id)
    return b ? b.name : `Book #${id}`
  }

  const userName = (id) => {
    const u = users.find((x) => x.id === id)
    return u ? u.name : `User #${id}`
  }

  const handleReturn = async (id) => {
    setBusy(id)
    setError('')
    setSuccess('')
    try {
      const updated = await api.returnBook(id)
      setSuccess(`Book returned. Fine: Rs.${updated.fineAmount || 0}`)
      await load()
    } catch (e) {
      setError(e.message)
    } finally {
      setBusy(null)
    }
  }

  return (
    <div>
      <h1>Returns</h1>
      <p className="subtitle">Books currently issued, ready to return</p>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={success} onClose={() => setSuccess('')} />

      <div className="card">
        <div className="toolbar">
          <span className="muted">{active.length} active issue(s)</span>
        </div>
        <Table
          columns={[
            { key: 'id', label: 'Issue ID' },
            { key: 'bookId', label: 'Book', render: (r) => bookName(r.bookId) },
            { key: 'userId', label: 'User', render: (r) => userName(r.userId) },
            { key: 'issueDate', label: 'Issue Date' },
            { key: 'status', label: 'Status', render: (r) => <span className="badge issued">{r.status}</span> }
          ]}
          rows={active}
          renderActions={(row) => (
            <button className="btn success" onClick={() => handleReturn(row.id)} disabled={busy === row.id}>
              {busy === row.id ? 'Returning...' : 'Return'}
            </button>
          )}
        />
      </div>
    </div>
  )
}
