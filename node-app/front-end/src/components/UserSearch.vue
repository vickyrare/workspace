<template>
  <v-text-field
    id="user_search_searchbox"
    label="Search"
    clearable
    append-outer-icon="search"
    v-model="searchText"
    class="searchBox"
  ></v-text-field>
</template>

<script>
// Shows the search input component
import { mapActions } from 'vuex'

export default {
  name: 'UserSearch',
  computed: {
    searchText: {
      get() {
        return this.$store.state.searchText
      },
      set(value) {
        this.setSearchText(value)
      },
    },
  },
  methods: {
    ...mapActions(['setSearchText']),
  },
}
</script>

<style>
.searchBox {
  width: 380px;
  height: 52px !important;
  opacity: 0.87;
  font-size: 14px;
  font-weight: 500;
  font-style: normal;
  font-stretch: normal;
  line-height: 1.43;
  letter-spacing: 0.3px;
  color: #181818;
}

.v-label {
  font-size: 14px !important;
}
</style>
