<template>
  <div>
    <h2 id="page-heading" data-cy="HelloWorldEntityHeading">
      <span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.home.title')" id="hello-world-entity-heading"></span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.home.refreshListLabel')"></span>
        </button>
        <router-link :to="{ name: 'HelloWorldEntityCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-hello-world-entity"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.home.createLabel')"></span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && helloWorldEntities && helloWorldEntities.length === 0">
      <span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.home.notFound')"></span>
    </div>
    <div class="table-responsive" v-if="helloWorldEntities && helloWorldEntities.length > 0">
      <table class="table table-striped" aria-describedby="helloWorldEntities">
        <thead>
          <tr>
            <th scope="row"><span v-text="t$('global.field.id')"></span></th>
            <th scope="row"><span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.name')"></span></th>
            <th scope="row"><span v-text="t$('helloWorldKeycloakApp.helloWorldEntity.testValue')"></span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="helloWorldEntity in helloWorldEntities" :key="helloWorldEntity.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'HelloWorldEntityView', params: { helloWorldEntityId: helloWorldEntity.id } }">{{
                helloWorldEntity.id
              }}</router-link>
            </td>
            <td>{{ helloWorldEntity.name }}</td>
            <td>{{ helloWorldEntity.testValue }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link
                  :to="{ name: 'HelloWorldEntityView', params: { helloWorldEntityId: helloWorldEntity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.view')"></span>
                  </button>
                </router-link>
                <router-link
                  :to="{ name: 'HelloWorldEntityEdit', params: { helloWorldEntityId: helloWorldEntity.id } }"
                  custom
                  v-slot="{ navigate }"
                >
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="t$('entity.action.edit')"></span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(helloWorldEntity)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="t$('entity.action.delete')"></span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <template #modal-title>
        <span
          id="helloWorldKeycloakApp.helloWorldEntity.delete.question"
          data-cy="helloWorldEntityDeleteDialogHeading"
          v-text="t$('entity.delete.title')"
        ></span>
      </template>
      <div class="modal-body">
        <p
          id="jhi-delete-helloWorldEntity-heading"
          v-text="t$('helloWorldKeycloakApp.helloWorldEntity.delete.question', { id: removeId })"
        ></p>
      </div>
      <template #modal-footer>
        <div>
          <button type="button" class="btn btn-secondary" v-text="t$('entity.action.cancel')" v-on:click="closeDialog()"></button>
          <button
            type="button"
            class="btn btn-primary"
            id="jhi-confirm-delete-helloWorldEntity"
            data-cy="entityConfirmDeleteButton"
            v-text="t$('entity.action.delete')"
            v-on:click="removeHelloWorldEntity()"
          ></button>
        </div>
      </template>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./hello-world-entity.component.ts"></script>
