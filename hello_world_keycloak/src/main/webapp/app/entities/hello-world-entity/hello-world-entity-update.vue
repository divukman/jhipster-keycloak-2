<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="helloWorldKeycloakApp.helloWorldEntity.home.createOrEditLabel"
          data-cy="HelloWorldEntityCreateUpdateHeading"
          v-text="t$('helloWorldKeycloakApp.helloWorldEntity.home.createOrEditLabel')"
        ></h2>
        <div>
          <div class="form-group" v-if="helloWorldEntity.id">
            <label for="id" v-text="t$('global.field.id')"></label>
            <input type="text" class="form-control" id="id" name="id" v-model="helloWorldEntity.id" readonly />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('helloWorldKeycloakApp.helloWorldEntity.name')"
              for="hello-world-entity-name"
            ></label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="hello-world-entity-name"
              data-cy="name"
              :class="{ valid: !v$.name.$invalid, invalid: v$.name.$invalid }"
              v-model="v$.name.$model"
            />
          </div>
          <div class="form-group">
            <label
              class="form-control-label"
              v-text="t$('helloWorldKeycloakApp.helloWorldEntity.testValue')"
              for="hello-world-entity-testValue"
            ></label>
            <input
              type="checkbox"
              class="form-check"
              name="testValue"
              id="hello-world-entity-testValue"
              data-cy="testValue"
              :class="{ valid: !v$.testValue.$invalid, invalid: v$.testValue.$invalid }"
              v-model="v$.testValue.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.cancel')"></span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="v$.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.save')"></span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./hello-world-entity-update.component.ts"></script>
