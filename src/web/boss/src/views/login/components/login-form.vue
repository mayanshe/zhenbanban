<template>
  <div class="login-form-wrapper">
    <div class="login-form-title">登陆 - 诊伴伴</div>
    <div class="login-form-sub-title">login</div>
    <div class="login-form-error-msg">{{ errorMessage }}</div>
    <a-form ref="loginForm" :model="credential" class="login-form" layout="vertical" @submit="handleSubmit">
      <a-form-item field="account" :rules="[{ required: true, message: '请输入账号' }]" :validate-trigger="['change', 'blur']" hide-label>
        <a-input v-model="credential.account" placeholder="用户名/手机号/邮箱" style="border-radius: 6px !important">
          <template #prefix>
            <icon-user />
          </template>
        </a-input>
      </a-form-item>
      <a-form-item field="password" :rules="[{ required: true, message: '请输入密码' }]" :validate-trigger="['change', 'blur']" hide-label>
        <a-input-password v-model="credential.password" placeholder="密码" allow-clear style="border-radius: 6px !important">
          <template #prefix>
            <icon-lock />
          </template>
        </a-input-password>
      </a-form-item>
      <a-space :size="16" direction="vertical">
        <div class="login-form-password-actions">
          <a-checkbox v-model="credential.rememberMe">记住我</a-checkbox>
          <a-link>忘记密码</a-link>
        </div>
        <a-button type="primary" html-type="submit" :loading="loading">登陆</a-button>
      </a-space>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import useLoading from '@/hooks/loading'
import { useUserStore } from '@/store'
import { Message } from '@arco-design/web-vue'
import { ValidatedError } from '@arco-design/web-vue/es/form/interface'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const errorMessage = ref('')
const { loading, setLoading } = useLoading()
const userStore = useUserStore()

// 管理员登陆请求信息
const credential = ref({
  account: '',
  password: '',
  rememberMe: false,
})

// 登陆请求
const handleSubmit = async ({ errors, values }: { errors: Record<string, ValidatedError> | undefined; values: Record<string, any> }) => {
  if (loading.value) return

  if (!errors) {
    setLoading(true)
    errorMessage.value = ''

    try {
      await userStore.login(credential.value)

      const { redirect, ...othersQuery } = router.currentRoute.value.query

      router.push({
        name: (redirect as string) || 'Workplace',
        query: {
          ...othersQuery,
        },
      })

      Message.success('登陆成功，欢迎回来！')
    } catch (err) {
      errorMessage.value = '登陆失败'
    } finally {
      setLoading(false)
    }
  }
}
</script>

<style lang="less" scoped>
.login-form {
  &-wrapper {
    width: 320px;
  }

  &-title {
    color: var(--color-text-1);
    font-weight: 500;
    font-size: 24px;
    line-height: 32px;
  }

  &-sub-title {
    color: var(--color-text-3);
    font-size: 16px;
    line-height: 24px;
  }

  &-error-msg {
    height: 32px;
    color: rgb(var(--red-6));
    line-height: 32px;
  }

  &-password-actions {
    display: flex;
    justify-content: space-between;
  }

  &-register-btn {
    color: var(--color-text-3) !important;
  }
}

:deep(.arco-btn) {
  border-radius: 8px !important;
  background: #20b99b !important;
}

:deep(.arco-input),
:deep(.arco-input-wrapper),
:deep(.arco-input-password) {
  border-radius: 6px !important;
}
</style>
