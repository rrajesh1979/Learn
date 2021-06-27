import rx.Observer;
import rx.Observable;
import rx.Subscriber;

public class HelloReactive {

    public static void main(String[] args) {

        Observable<String> createObserver = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                APIContainer apiContainer = new APIContainer();
                apiContainer.testAPI();
                subscriber.onNext("Hello World");
                subscriber.onCompleted();
            }
        });

        Subscriber<String> mySubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                System.out.println("MySubscriber onNext(): "+ s);
            }

            @Override
            public void onCompleted() {
                System.out.println("Subscriber completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("OnError");
            }
        };

        createObserver.subscribe(mySubscriber);
    }
}