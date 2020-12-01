<template>
  <v-main>
    <v-container class="px-9">
      <v-layout
          wrap
          align-start justify-center row fill-height
      >
        <v-flex xs12>
          <v-container pa-0>
            <v-layout col>
              <v-flex>
                <h1 class="pageTitle">About</h1>
              </v-flex>
            </v-layout>
          </v-container>
          <v-flex>
            <v-list>
              <v-list-item>
                <v-list-item-content>
                    <v-list-item-subtitle>
                      MCP UI v{{ VUE_APP_VERSION }}<br />
                      Copyright © 2019 - 2020 Trapeze Group Asia Pacific Pty Ltd <br />
                    </v-list-item-subtitle>
                  </v-list-item-content>
              </v-list-item>
            </v-list>
          </v-flex>
        </v-flex>
      </v-layout>
    </v-container>
  </v-main>
</template>
<script>

export default {
  name: 'About',
  data() {
    return {
      VUE_APP_VERSION: process.env.VUE_APP_VERSION,
      VUE_APP_SCM_VERSION: process.env.VUE_APP_SCM_VERSION === 0 ? '' : 'hg' + process.env.VUE_APP_SCM_VERSION,
    }
  },
}
</script>

