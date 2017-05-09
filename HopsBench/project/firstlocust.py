from locust import Locust, events, TaskSet, task
from snakebite.client import Client
import time


class SnakebiteClient(Client):

    def __getattr__(self, name):
        func = Client.__getattr__(self, name)
        #if not callable(func):
        #    return func

        def wrapper(*args, **kwargs):
            print "Called " + name
            start_time = time.time()
            try:
                result = func(*args, **kwargs)
            except Exception as e:
                total_time = int((time.time() - start_time) * 1000)
                events.request_failure.fire(request_type="snakebite", name=name, response_time=total_time, exception=e)
            else:
                total_time = int((time.time() - start_time) * 1000)
                events.request_success.fire(request_type="snakebite", name=name, response_time=total_time,
                                            response_length=0)
                print "Returning from " + name
                return result

                #Response length is hardcoded.. might want to handle it somehow
        return wrapper

   
class SnakebiteLocust(Locust):
    def __init__(self, *args, **kwargs):
        super(SnakebiteLocust, self).__init__(*args, **kwargs)
        self.client = Client(host=self.host, port=self.port, use_trash=self.use_trash)


class MyTaskSet(TaskSet):

    @task(1)
    def write_random_file(self):
        dict(self.client.touchz(["/"+str(time.time())+".test"]))

    @task(10)
    def my_task(self):
        list(self.client.ls(['/']))


class ApiUser(SnakebiteLocust):
    host = "august-OptiPlex-7010"
    port = 26801
    use_trash = False

    task_set = MyTaskSet
    min_wait = 5000
    max_wait = 9000


