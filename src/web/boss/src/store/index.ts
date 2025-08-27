import { createPinia } from 'pinia'
import useAppStore from './modules/app'
import useTabBarStore from './modules/tab-bar'
import useUserStore from './modules/admin'

const pinia = createPinia()

export { useAppStore, useTabBarStore, useUserStore }
export default pinia
