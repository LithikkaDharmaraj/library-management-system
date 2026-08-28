import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Table from '../components/Table.jsx'
import Alert from '../components/Alert.jsx'

export default function Fines() {
  const [fines, setFines] = useState([])
  const [issues, setIssues] = useState([])
  const [books, setBooks] = useState([])
  const [users, setUsers] = useState([])
  const [error, setError] = useState('')

  const load = useCallback(async () => {
    try {
      const [f, i, b, u] = await Promise.all([api.getFines(), api.getIssues(), api.getBooks(), api.getUsers()])
      setFines(f)
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

  const issueById = (id) => issues.find((x) => x.id === id)
  const bookName = (id) => {
    const b = books.find((x) => x.id === id)
    return b ? b.name : `Book #${id}`
  }
  const userName = (id) => {
    const u = users.find((x) => x.id === id)
    return u ? u.name : `User #${id}`
  }

  const rowsWithDetails = fines.map((f) => {
    const issue = issueById(f.issuedId)
    return { ...f, issue }
  })

  return (
    <div>
      <h1>Fines</h1>
      <p className="subtitle">Fine records for issue/return transactions</p>

      <Alert type="error" message={error} onClose={() => setError('')} />

      <div className="card">
        <Table
          columns={[
            { key: 'issuedId', label: 'Issue ID' },
            {
              key: 'book',
              label: 'Book',
              render: (r) => (r.issue ? bookName(r.issue.bookId) : `Issue #${r.issuedId}`)
            },
            {
              key: 'user',
              label: 'User',
              render: (r) => (r.issue ? userName(r.issue.userId) : '—')
            },
            { key: 'allowedDays', label: 'Allowed Days' },
            { key: 'finePerDay', label: 'Fine / Day' },
            {
              key: 'fineAmount',
              label: 'Fine Amount',
              render: (r) => (
                <span>{r.fineAmount || 0}</span>
              )
            },
            {
              key: 'status',
              label: 'Overdue',
              render: (r) => (r.fineAmount > 0 ? <span className="badge overdue">Yes</span> : <span className="muted">No</span>)
            }
          ]}
          rows={rowsWithDetails}
        />
      </div>
    </div>
  )
}
