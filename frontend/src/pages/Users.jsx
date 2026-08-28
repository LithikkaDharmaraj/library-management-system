import { useCallback, useEffect, useState } from 'react'
import { api } from '../api.js'
import Table from '../components/Table.jsx'
import Modal from '../components/Modal.jsx'
import Alert from '../components/Alert.jsx'

const emptyUser = { name: '', userType: 'student', empRollNo: '' }

export default function Users() {
  const [users, setUsers] = useState([])
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState(null)
  const [form, setForm] = useState(emptyUser)
  const [saving, setSaving] = useState(false)

  const load = useCallback(async () => {
    try {
      setUsers(await api.getUsers())
    } catch (e) {
      setError(e.message)
    }
  }, [])

  useEffect(() => {
    load()
  }, [load])

  const openAdd = () => {
    setEditing(null)
    setForm(emptyUser)
    setModalOpen(true)
  }

  const openEdit = (user) => {
    setEditing(user)
    setForm({ name: user.name, userType: user.userType, empRollNo: user.empRollNo })
    setModalOpen(true)
  }

  const handleSave = async (e) => {
    e.preventDefault()
    setSaving(true)
    setError('')
    try {
      if (editing) {
        await api.updateUser(editing.id, form)
        setSuccess(`User updated: ${form.name}`)
      } else {
        await api.addUser(form)
        setSuccess(`User added: ${form.name}`)
      }
      setModalOpen(false)
      await load()
    } catch (e2) {
      setError(e2.message)
    } finally {
      setSaving(false)
    }
  }

  const handleDelete = async (user) => {
    if (!window.confirm(`Delete user "${user.name}"?`)) return
    try {
      await api.deleteUser(user.id)
      setSuccess(`User deleted: ${user.name}`)
      await load()
    } catch (e) {
      setError(e.message)
    }
  }

  return (
    <div>
      <h1>Users</h1>
      <p className="subtitle">Manage library members</p>

      <Alert type="error" message={error} onClose={() => setError('')} />
      <Alert type="success" message={success} onClose={() => setSuccess('')} />

      <div className="card">
        <div className="toolbar">
          <span className="muted">{users.length} user(s)</span>
          <button className="btn" onClick={openAdd}>+ Add User</button>
        </div>
        <Table
          columns={[
            { key: 'id', label: 'ID' },
            { key: 'name', label: 'Name' },
            { key: 'userType', label: 'Type' },
            { key: 'empRollNo', label: 'Emp / Roll No' }
          ]}
          rows={users}
          renderActions={(row) => (
            <>
              <button className="btn secondary" onClick={() => openEdit(row)}>Edit</button>
              <button className="btn danger" onClick={() => handleDelete(row)}>Delete</button>
            </>
          )}
        />
      </div>

      {modalOpen && (
        <Modal title={editing ? 'Edit User' : 'Add User'} onCancel={() => setModalOpen(false)}>
          <form onSubmit={handleSave}>
            <div className="field">
              <label>Name</label>
              <input required value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />
            </div>
            <div className="field">
              <label>User Type</label>
              <select value={form.userType} onChange={(e) => setForm({ ...form, userType: e.target.value })}>
                <option value="student">Student</option>
                <option value="faculty">Faculty</option>
                <option value="staff">Staff</option>
                <option value="other">Other</option>
              </select>
            </div>
            <div className="field">
              <label>Emp / Roll No</label>
              <input required value={form.empRollNo} onChange={(e) => setForm({ ...form, empRollNo: e.target.value })} />
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
