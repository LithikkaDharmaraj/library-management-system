import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Table from '../components/Table.jsx'
import Modal from '../components/Modal.jsx'
import Alert from '../components/Alert.jsx'

const emptyBook = { name: '', author: '', isbn: '' }

export default function Books() {
  const [books, setBooks] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(emptyBook)
  const [saving, setSaving] = useState(false)

  const load = useCallback(async () => {
    try {
      setBooks(await api.getBooks())
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const openAdd = () => {
    setEditing(null)
    setForm(emptyBook)
    setModalOpen(true)
  }

  const openEdit = (book) => {
    setEditing(book)
    setForm({ name: book.name, author: book.author, isbn: book.isbn })
    setModalOpen(true)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      if (editing) {
        await api.updateBook(editing.id, form)
        setSuccess(`Book updated: ${form.name}`)
      } else {
        await api.addBook(form)
        setSuccess(`Book added: ${form.name}`)
      }
      setModalOpen(false)
      await load()
    } catch (e2) {
      setError(e2.message)
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (book) => {
    if (!window.confirm(`Delete book "${book.name}"?`)) return
    try {
      await api.deleteBook(book.id)
      setSuccess(`Book deleted: ${book.name}`)
      await load()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div>
      <h1>Books</h1>
      <p className="subtitle">Manage the book catalog</p>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={success} onClose={() => setSuccess('')} />

      <div className="card">
        <div className="toolbar">
          <span className="muted">{books.length} book(s)</span>
          <button className="btn" onClick={openAdd}>+ Add Book</button>
        </div>
        <Table
          columns={[
            { key: 'id', label: 'ID' },
            { key: 'name', label: 'Name' },
            { key: 'author', label: 'Author' },
            { key: 'isbn', label: 'ISBN' }
          ]}
          rows={books}
          renderActions={(row) => (
            <>
              <button className="btn secondary" onClick={() => openEdit(row)}>Edit</button>
              <button className="btn danger" onClick={() => handleDelete(row)}>Delete</button>
            </>
          )}
        />
      </div>

      {modalOpen && (
        <Modal title={editing ? 'Edit Book' : 'Add Book'} onCancel={() => setModalOpen(false)}>
          <form onSubmit={handleSave}>
            <div className="field">
              <label>Name</label>
              <input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            </div>
            <div className="field">
              <label>Author</label>
              <input required value={form.author} onChange={(e) => setForm({ ...form, author: e.target.value })} />
            </div>
            <div className="field">
              <label>ISBN</label>
              <input required value={form.isbn} onChange={(e) => setForm({ ...form, isbn: e.target.value })} />
            </div>
            <div className="modal-actions">
              <button type="button" className="btn secondary" onClick={() => setModalOpen(false)}>Cancel</button>
              <button type="submit" className="btn" disabled={saving}>{saving ? 'Saving...' : 'Save'}</button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  )
}
