<script setup>
import { ref } from "vue";
import { UserOutlined, LockOutlined, MailOutlined } from "@ant-design/icons-vue";
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
import { getAction } from "@/api/manage";
// 获取路由实例
const router = useRouter();
const registerForm = ref();
defineOptions({
  name: "RegisterPage",
});
const registerFormState = ref({
  username: "",
  password: "",
  rePassword: "",
  email: "",
  verifyCode: "",
});

// 发送按钮可用性
const sendBtnAvailable = ref(true);

// 用户名校验
const validateUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入用户名"));
    return;
  }

  const reg = /^[a-zA-Z0-9\u4E00-\u9FA5]{4,16}$/;
  if (!reg.test(value)) {
    callback(new Error("用户名需要4到16位不含特殊字符"));
    return;
  }

  // 校验通过
  callback();
};

// 密码校验
const validatePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入密码"));
    return;
  }

  // 长度校验
  if (value.length < 8 || value.length > 16) {
    callback(new Error("密码长度需在8到16个字符之间"));
    return;
  }

  // 字符类型校验
  const hasLowercase = /[a-z]/.test(value); // 包含小写字母
  const hasNumber = /[0-9]/.test(value); // 包含数字
  const hasSpecial = /[^A-Za-z0-9]/.test(value); // 包含特殊符号

  if (!hasLowercase) {
    callback(new Error("密码必须包含小写字母"));
    return;
  }

  if (!hasNumber) {
    callback(new Error("密码必须包含数字"));
    return;
  }

  if (!hasSpecial) {
    callback(new Error("密码必须包含特殊符号"));
    return;
  }

  // 校验通过
  callback();
};

// 再次输入密码校验
const validateRePassword = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入密码"));
    return;
  }
  if (value !== registerFormState.value.password) {
    callback(new Error("两次输入密码不一致"));
    return;
  }
  callback();
};
// 验证码校验
const validateVerifyCode = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入验证码"));
    return;
  }

  // 长度校验
  if (value.length !== 4) {
    callback(new Error("验证码长度应为6位"));
    return;
  }

  // 内容校验：6位数字
  const isValid = /^[0-9]{6}$/.test(value);
  if (!isValid) {
    callback(new Error("验证码只能包含数字"));
    return;
  }

  // 校验通过
  callback();
};

// 邮箱校验
const validateEmail = (rule, value, callback) => {
  if (!value) {
    callback(new Error("请输入邮箱"));
    return;
  }

  // 邮箱格式正则表达式
  const emailReg = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:\.[a-zA-Z]{2,})?$/;

  if (!emailReg.test(value)) {
    callback(new Error("请输入有效的邮箱地址"));
    return;
  }

  // 校验通过
  callback();
};
const rules = {
  username: [
    {
      required: true,
      validator: validateUsername,
      trigger: "blur",
    },
  ],
  password: [
    {
      required: true,
      validator: validatePassword,
      trigger: "blur",
    },
  ],
  rePassword: [
    {
      required: true,
      validator: validateRePassword,
      trigger: "blur",
    },
  ],
  email: [
    {
      required: true,
      validator: validateEmail,
      trigger: "blur",
    },
  ],
  verifyCode: [
    {
      required: true,
      validator: validateVerifyCode,
      trigger: "blur",
    },
  ],
};

// 实现发送验证码的请求
// 页面输入邮箱,点击按钮,发送请求,携带邮箱参数,往邮箱里发送验证码,
// 用户在邮箱里收到验证码,填写在表单上,然后一起提交注册
const countdown = ref(0); // 倒计时秒数
// const timestamp = new Date().getTime();
const handleSendVerifyCode = async() => {
  try {
    // const values = await registerForm.value.validateEmail
    const values = await registerForm.value.validateFields(
      ['email'], {force: true}
    )

    // 验证通过后执行
    const res = await getAction('/user/verify/code', {email: values.email})
    if (res.code === 200) {
      message.success('验证码发送成功，注意查收')
      countdown.value = 60
      sendBtnAvailable.value = false
      const timer = setInterval(() => {
        countdown.value--
        if (countdown.value <= 0) {
          clearInterval(timer)
          sendBtnAvailable.value = true
        }
      }, 1000);
    }else {
      message.error(res.message || '验证码发送失败')
    }
  }catch (error) {
    console.error('验证码发送失败:' + error)
  }
}
</script>

<template>
  <a-layout style="height: 100%; background-color: white">
    <a-layout-header class="form-header">
      <h1>注册表单</h1>
    </a-layout-header>
    <a-layout-content>
      <a-form
        :model="registerFormState"
        ref="registerForm"
        :rules="rules"
        name="normal_register"
        class="register-form"
        @finish="onFinish"
        @finishFailed="onFinishFailed"
      >
        <a-form-item name="username">
          <a-input
            v-model:value="registerFormState.username"
            size="large"
            placeholder="请输入用户名"
          >
            <template #prefix>
              <UserOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="password">
          <a-input-password
            v-model:value="registerFormState.password"
            size="large"
            placeholder="请输入密码"
          >
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="rePassword">
          <a-input-password
            v-model:value="registerFormState.rePassword"
            size="large"
            placeholder="请再次输入密码"
          >
            <template #prefix>
              <LockOutlined class="site-form-item-icon" />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="email">
          <a-input size="large" placeholder="请输入邮箱" v-model:value="registerFormState.email">
            <template #prefix>
              <MailOutlined class="site-form-item-icon" />
            </template>
          </a-input>
        </a-form-item>

        <a-form-item name="verifyCode">
          <a-row justify="space-between" gutter="16">
            <a-col :span="12">
              <!-- 稍微减小宽度，留出间距 -->
              <a-input
                size="large"
                placeholder="请输入验证码"
                v-model:value="registerFormState.verifyCode"
              ></a-input>
            </a-col>
            <a-col :span="11" :offset="1">
              <!-- 稍微减小宽度，留出间距 -->
              <a-button size="large" :disable="sendBtnAvailable" @click="handleSendVerifyCode">
                {{ sendBtnAvailable ? "发送验证码" : `重新发送(${countdown})` }}</a-button
              >
            </a-col>
          </a-row>
        </a-form-item>

        <a-form-item class="register-btn-row">
          <a-button
            :disabled="disabled"
            type="primary"
            size="large"
            html-type="submit"
            class="register-form-btn"
          >
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
