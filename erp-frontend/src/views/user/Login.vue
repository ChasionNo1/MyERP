<script setup>
import { UserOutlined, LockOutlined} from '@ant-design/icons-vue';
defineOptions({
  name: 'LoginPage'
})
import { reactive, computed } from 'vue';
const formState = reactive({
  username: '',
  password: '',
  remember: false,
});
const onFinish = values => {
  console.log('Success:', values);
};
const onFinishFailed = errorInfo => {
  console.log('Failed:', errorInfo);
};
const disabled = computed(() => {
  return !(formState.username && formState.password);
});
</script>

<template>
  <a-layout style="height: 100%; background-color: white;">
    <a-layout-header class="form-header"><h1>登录表单</h1></a-layout-header>
    <a-layout-content>
      <a-form :model="formState" name="normal_login" class="login-form" @finish="onFinish" 
        @finishFailed="onFinishFailed">
        <a-form-item name="username"
          :rules="[{ required: true, message: '请输入你的用户名!' }]">
          <a-input v-model:value="formState.username" size="large">
            <template #prefix>
              <UserOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password"
          :rules="[{ required: true, message: 'Please input your password!' }]">
          <a-input-password v-model:value="formState.password" size="large">
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>
        <!-- 验证码行 - 使用 a-row 和 a-col 布局 -->
        <a-form-item name="verifyCode">
          <a-row gutter="16">
            <a-col :span="12">
              <a-input size="large" placeholder="请输入验证码"></a-input>
            </a-col>
            <a-col :span="12">
              <img src="https://picsum.photos/120/40" alt="验证码" class="verify-code-img" />
            </a-col>
          </a-row>
        </a-form-item>

        <!-- 记住我和忘记密码行 - 使用flex布局 -->
        <a-form-item class="remember-row-wrapper">
          <div class="remember-row">
            <a-form-item name="remember" no-style>
              <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
            </a-form-item>
            <a class="login-form-forgot" href="">忘记密码</a>
          </div>
        </a-form-item>

        <a-form-item class="login-btn-row">
          <a-button :disabled="disabled" type="primary" size="large" html-type="submit" class="login-form-button">
            登录
          </a-button>
        </a-form-item>
      </a-form>
    </a-layout-content>
    <a-layout-footer class="form-footer">
      还没有账号?<router-link to="/user/register">立即注册</router-link>
    </a-layout-footer>
  </a-layout>
</template>

<style scoped>
.form-header {
  background-color: white;
  text-align: center;
}
.login-form {
  max-width: 380px; /* 增加宽度以适应验证码区域 */
  margin: 0 auto; /* 水平居中 */
}

/* 验证码图片样式 */
.verify-code-img {
  width: 100%;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
  cursor: pointer;
}

/* 修复记住我/忘记密码行的两端对齐 */
.remember-row-wrapper {
  padding: 0 !important; /* 移除form-item的内边距 */
}

.remember-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

/* 登录按钮样式 */
.login-btn-row {
  margin-bottom: 0;
  width: 100%;
}
/* 登录按钮占满宽度 */
.login-form-button {
  width: 100%;
}

/* 页脚样式 */
.form-footer {
  text-align: center;
  background-color: white;
  padding: 16px 0;
}

</style>