<script setup>
import { ref } from 'vue'
import { UserOutlined, LockOutlined, MailOutlined } from '@ant-design/icons-vue';
import { useRouter } from 'vue-router'

// 获取路由实例
const router = useRouter()

defineOptions({
  name: 'RegisterPage'
})
const registerFormState = ref({
  username: '',
  password: '',
  rePassword: '',
  email: '',
  verifyCode: '',
})
</script>

<template>
  <a-layout style="height: 100%; background-color: white;">
    <a-layout-header class="form-header">
      <h1>注册表单</h1>
    </a-layout-header>
    <a-layout-content>
      <a-form :model="registerFormState" name="normal_register" class="register-form" @finish="onFinish"
        @finishFailed="onFinishFailed">
        <a-form-item name="username" :rules="[{ required: true, message: '请输入你的用户名!' }]">
          <a-input v-model:value="registerFormState.username" size="large" placeholder="请输入用户名">
            <template #prefix>
              <UserOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password" :rules="[{ required: true, message: 'Please input your password!' }]">
          <a-input-password v-model:value="registerFormState.password" size="large" placeholder="请输入密码">
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="rePassword" :rules="[{ required: true, message: 'Please input your password!' }]">
          <a-input-password v-model:value="registerFormState.rePassword" size="large" placeholder="请再次输入密码">
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item>
          <a-input size="large" placeholder="请输入邮箱">
            <template #prefix>
              <MailOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item>
          <a-row justify="space-between" gutter="16">
            <a-col :span="12"> <!-- 稍微减小宽度，留出间距 -->
              <a-input size="large" placeholder="请输入验证码"></a-input>
            </a-col>
            <a-col :span="11" :offset="1"> <!-- 稍微减小宽度，留出间距 -->
              <a-button size="large">发送验证码</a-button>
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item class="register-btn-row">
          <a-button :disabled="disabled" type="primary" size="large" html-type="submit" class="register-form-btn">
            注册
          </a-button>
        </a-form-item>
      </a-form>
    </a-layout-content>
    <a-layout-footer class="form-footer">
  已有账号? <router-link to="/user/login">立即登录</router-link>
</a-layout-footer>
  </a-layout>
</template>

<style scoped>
.form-header {
  background-color: white;
  text-align: center;
}

.register-btn-row {
  margin-bottom: 0;
  width: 100%;
}

.register-form-btn {
  width: 100%;
}
.form-footer {
  text-align: center;
  background-color: white;
  padding: 16px 0;
}
</style>