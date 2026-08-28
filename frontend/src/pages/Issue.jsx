import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Alert from '../components/Alert.jsx'

export default function Issue() {
  const [books, setBooks] = useState([])
  const [users, setUsers] = useState([])
  const [issuedIds, setIssuedIds] = useState([])
  const [bookId, setBookId] = useState('')
  const [userId, setUserId] = useState('')
  const [allowedDays, setAllowedDays] = useState(10)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [busy, setBusy] = useState(false)

  const load = useCallback(async () => {
    try {
      const [b, u, i] = await Promise.all([api.getBooks(), api.getUsers(), api.getIssues()])
      setBooks(b)
      setUsers(u)
      setIssuedIds(i.filter((x) => x.status === 'ISSUED').map((x) => x.bookId))
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const availableBooks = books.filter((b) => !issuedIds.includes(b.id))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setSuccess('')
    if (!bookId || !userId) {
      setError('Please select both a book and a user.')
      return
    }
    setBusy(true)
    try {
      await api.issueBook({ bookId: Number(bookId), userId: Number(userId), allowedDays: Number(allowedDays) || 10 })
      setSuccess('Book issued successfully.')
      setBookId('')
      await load()
    } catch (e2) {
      setError(e2.message)
    } finally {
      setBusy(false)
    }
  }

  return (
    <div>
      <h1>Issue a Book</h1>
      <p className="subtitle">Assign an available book to a user</p>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={success} onClose={() => setSuccess('')} />

      <div className="card" style={{ maxWidth: 480 }}>
        <form onSubmit={handleSubmit}>
          <div className="field">
            <label>Book</label>
            <select value={bookId} onChange={(e) => setBookId(e.target.value)}>
              <option value="">-- Select book --</option>
              {availableBooks.map((b) => (
                <option key={b.id} value={b.id}>{b.name} ({b.author})</option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>User</label>
            <select value={userId} onChange={(e) => setUserId(e.target.value)}>
              <option value="">-- Select user --</option>
              {users.map((u) => (
                <option key={u.id} value={u.id}>{u.name} ({u.empRollNo})</option>
              ))}
            </select>
          </div>
          <div className="field">
            <label>Allowed Days (default 10)</label>
            <input type="number" min="1" value={allowedDays} onChange={(e) => setAllowedDays(e.target.value)} />
          </div>
          <button type="submit" className="btn success" disabled={busy}>
            {busy ? 'Issuing...' : 'Issue Book'}
          </button>
        </form>
      </div>
    </div>
  )
}
