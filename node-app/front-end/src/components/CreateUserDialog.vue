<template>
  <v-dialog
    v-model="localDialog"
    fullscreen
    hide-overlay
    transition="dialog-bottom-transition"
  >
    <template v-slot:activator="{ on }">
      <span color="primary" dark v-on="on" v-if="!isEditing">Create User</span
      ><v-icon icon medium v-on="on" @focus="$event.target.style='outline: 2px dashed blue;'" @blur="$event.target.style='outline: none;'" v-else>edit</v-icon>
    </template>
    <v-card>
    <div>
      <v-toolbar color="black" dark fixed>
        <v-app-bar-nav-icon @click="close">
          <v-icon>close</v-icon>
        </v-app-bar-nav-icon>
        <v-toolbar-title>{{ this.mode }} User</v-toolbar-title>
        <v-spacer></v-spacer>
        <v-toolbar-items>
          <v-btn dark text @click="onSave">Save</v-btn>
        </v-toolbar-items>
      </v-toolbar>
      <v-card style="padding-top: 40px;" text>
        <v-container grid-list-md>
          <v-layout wrap>
            <v-flex xs8>
              <v-form ref="form" v-model="valid" lazy-validation>
                <small>*indicates required field</small>
                <v-flex class="inputSection">
                  <v-text-field
                    :id="'create_user_id_' + mode + '_' + user.user_id"
                    v-if="isEditing"
                    label="ID"
                    type="text"
                    v-model="localUser.user_id"
                    readonly
                  ></v-text-field>
                  <v-text-field
                    :id="'create_user_first_name_' + mode + '_' + user.user_id"
                    label="Firstname*"
                    type="text"
                    v-model="localUser.first_name"
                    :rules="firstNameRules"
                    required
                  ></v-text-field>
                  <v-text-field
                    :id="'create_user_last_name_' + mode + '_' + user.user_id"
                    label="Lastname*"
                    type="text"
                    v-model="localUser.last_name"
                    :rules="lastNameRules"
                    required
                  ></v-text-field>
                  <v-text-field
                    :id="'create_user_email_' + mode + '_' + user.user_id"
                    label="Email*"
                    type="text"
                    v-model="localUser.email"
                    :rules="emailRules"
                    required
                  >
                  </v-text-field>
                  <v-text-field
                    :id="'create_user_password_' + mode + '_' + user.user_id"
                    v-if="!isEditing"
                    label="Password*"
                    type="password"
                    v-model="localUser.password"
                    :rules="passwordRules"
                    required
                  >
                  <v-tooltip slot="append" bottom>
                    <template v-slot:activator="{ on }"> 
                      <v-icon v-on="on" slot="activator" color="primary" dark>help</v-icon>
                    </template>
                    <span>{{passwordPolicy}}</span>
                  </v-tooltip>
                  </v-text-field>
                  <v-text-field
                    id="create_user_confirm_password_"
                    v-if="!isEditing"
                    label="Confirm password*"
                    type="password"
                    v-model="localUser.confirm_password"
                    :rules="confirmPasswordRules"
                    required
                  ></v-text-field>
                  <v-select
                    :id="'create_user_select_role_' + mode + '_' + user.user_id"
                    :items="availableRoles"
                    label="Role*"
                    required
                    v-model="localUser.role"
                    item-text="name"
                    item-value="id"
                    attach
                  ></v-select>
                </v-flex>
              </v-form>
            </v-flex>
          </v-layout>
        </v-container>
      </v-card>
    </div>
    </v-card>
  </v-dialog>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'CreateUserDialog',
  props: {
    mode: {
      type: String,
      default: 'Create',
    },
    user: {
      type: Object,
      default: () => ({
        user_id: '',
        first_name: '',
        last_name: '',
        email: '',
        password: '',
        role: '',
      }),
    },
    dialog: Boolean,
  },
  computed: {
    isEditing() {
      return this.mode === 'Edit'
    }
  },
  data() {
    return {
      localDialog: false,
      valid: true,
      localUser: {
        user_id: '',
        first_name: '',
        last_name: '',
        password: '',
        confirm_password: '',
        email: '',
        role: ''
      },
      availableRoles: [
        {
          id: 'user',
          name: 'User',
        },
        {
          id: 'admin',
          name: 'Admin',
        }
      ],
      passwordPolicy: 'Password policy: Password must be at least 8 characters long, and include at least one digit, at least one lower case character, at least one upper case character, at least one special character from !@#$%^&*()_+',
      firstNameRules: [v => !!v || 'First name is required'],
      lastNameRules: [v => !!v || 'Last name is required'],
      emailRules: [
        v => !!v || 'Email is required',
        v => /.+@.+/.test(v) || 'Email must be a valid email',
      ],
      passwordRules: [v => !!v || 'Password is required'],
      confirmPasswordRules: [
        v =>
          (!!v && v) === this.localUser.password ||
          'Password does not match',
      ]
    }
  },
  watch: {
    dialog: function(newVal, oldVal) {
      this.localDialog = newVal
    },
    localDialog: function(newVal, oldVal) {
      if (newVal) {
        this.localUser = {
          user_id: this.user.user_id,
          first_name: this.user.first_name,
          last_name: this.user.last_name,
          email:this.user.email,
          role: this.user.role
        }
      }
    }
  },
  methods: {
    ...mapActions(['addUser', 'editUser', 'hideExportMenu']),
    onSave() {
      if (this.$refs.form.validate()) {
        this.snackbar = true

        var vm = this
        if (this.mode !== 'Edit') {
          this.addUser(this.localUser)
            .then(() => {
              vm.error = null
              this.close()
              this.$toast.success('User created', { icon: 'done' })
            })
            .catch(error => {
            this.$toast.error(error, { 
              icon: 'error', showClose: true, timeout: 10000
            })
            })
        } else {
          this.editUser(this.localUser)
            .then(() => {
              vm.error = null
              this.close()
              this.$toast.success('User updated', { icon: 'done' })
            })
            .catch(error => {
            this.$toast.error(error, { 
              icon: 'error', showClose: true, timeout: 10000
            })
          })
        }
      }
    },
    reset() {
      this.$refs.form.reset()
    },
    resetValidation() {
      this.$refs.form.resetValidation()
    },
    close() {
      this.hideExportMenu()
      this.localDialog = false
      this.$emit('update:dialog', this.localDialog)
    }
  },
}
</script>

<style>
</style>
