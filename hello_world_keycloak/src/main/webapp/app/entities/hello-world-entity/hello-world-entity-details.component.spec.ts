/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import HelloWorldEntityDetails from './hello-world-entity-details.vue';
import HelloWorldEntityService from './hello-world-entity.service';
import AlertService from '@/shared/alert/alert.service';

type HelloWorldEntityDetailsComponentType = InstanceType<typeof HelloWorldEntityDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const helloWorldEntitySample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('HelloWorldEntity Management Detail Component', () => {
    let helloWorldEntityServiceStub: SinonStubbedInstance<HelloWorldEntityService>;
    let mountOptions: MountingOptions<HelloWorldEntityDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      helloWorldEntityServiceStub = sinon.createStubInstance<HelloWorldEntityService>(HelloWorldEntityService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          helloWorldEntityService: () => helloWorldEntityServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        helloWorldEntityServiceStub.find.resolves(helloWorldEntitySample);
        route = {
          params: {
            helloWorldEntityId: '' + 123,
          },
        };
        const wrapper = shallowMount(HelloWorldEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.helloWorldEntity).toMatchObject(helloWorldEntitySample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        helloWorldEntityServiceStub.find.resolves(helloWorldEntitySample);
        const wrapper = shallowMount(HelloWorldEntityDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
