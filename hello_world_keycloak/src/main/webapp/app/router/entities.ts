import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const HelloWorldEntity = () => import('@/entities/hello-world-entity/hello-world-entity.vue');
const HelloWorldEntityUpdate = () => import('@/entities/hello-world-entity/hello-world-entity-update.vue');
const HelloWorldEntityDetails = () => import('@/entities/hello-world-entity/hello-world-entity-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'hello-world-entity',
      name: 'HelloWorldEntity',
      component: HelloWorldEntity,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'hello-world-entity/new',
      name: 'HelloWorldEntityCreate',
      component: HelloWorldEntityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'hello-world-entity/:helloWorldEntityId/edit',
      name: 'HelloWorldEntityEdit',
      component: HelloWorldEntityUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'hello-world-entity/:helloWorldEntityId/view',
      name: 'HelloWorldEntityView',
      component: HelloWorldEntityDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
