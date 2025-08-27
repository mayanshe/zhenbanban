import { Credential, getAdminState, login as adminrLogin, logout as adminLogout } from '@/api/admin'
import { clearToken, setToken } from '@/utils/auth'
import { removeRouteListener } from '@/utils/route-listener'
import { defineStore } from 'pinia'
import useAppStore from '../app'
import { AdminState } from './types'

const useUsertore = defineStore('admin', {
  state: (): AdminState => ({
    username: undefined,
    surname: undefined,
    givenName: undefined,
    gender: undefined,
    email: undefined,
    phone: undefined,
    avatar: undefined,
  }),

  getters: {
    adminState(state: AdminState): AdminState {
      return { ...state }
    },
  },

  actions: {
    setState(partial: Partial<AdminState>) {
      this.$patch(partial)
    },

    resetState() {
      this.$reset()
    },

    async state() {
      const res = await getAdminState()
      this.setState(res.data)
    },

    async login(credential: Credential) {
      try {
        const res = await adminrLogin(credential)
        const token = `${res.type} ${res.token}`
        setToken(token)
      } catch (err) {
        clearToken()
        throw err
      }
    },

    logoutCallBack() {
      const appStore = useAppStore()
      this.resetState()
      clearToken()
      removeRouteListener()
      appStore.clearServerMenu()
    },

    async logout() {
      try {
        await adminLogout()
      } finally {
        this.logoutCallBack()
      }
    },
  },
})

export default useUsertore
